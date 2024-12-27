package com.grit.backend.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.grit.frontend.util.JsonUtil;
import com.grit.model.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InMemoryTaskRepository implements TaskRepository {
    private static final Logger LOGGER = Logger.getLogger(InMemoryTaskRepository.class.getName());
    private final Map<Integer, Task> taskMap = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    // Path to the tasks.json file in the same folder as MainApp.java
    private static final String JSON_FILE_PATH = "tasks.json";

    public InMemoryTaskRepository() {
        loadDemoTasksFromJson(); // Load demo tasks when the repository is initialized
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public Optional<Task> findById(int id) {
        return Optional.ofNullable(taskMap.get(id));
    }

    @Override
    public synchronized Task save(Task task) {
        if (task.getId() == 0) {
            task.setId(getNextId()); // Auto-generate ID for new tasks
        }
        taskMap.put(task.getId(), task);
        saveToJsonFile(); // Save to JSON file
        return task;
    }

    @Override
    public synchronized Task update(Task task) {
        if (!taskMap.containsKey(task.getId())) {
            throw new IllegalArgumentException("Task not found for update");
        }
        taskMap.put(task.getId(), task);
        saveToJsonFile(); // Save to JSON file
        return task;
    }

    @Override
    public synchronized boolean deleteById(int id) {
        boolean removed = taskMap.remove(id) != null;
        if (removed) {
            saveToJsonFile(); // Save to JSON file
        }
        return removed;
    }

    // Load tasks from JSON file
    private void loadDemoTasksFromJson() {
        Path path = Paths.get(JSON_FILE_PATH);
        LOGGER.info("Attempting to load tasks from JSON file: " + path.toAbsolutePath());

        if (!Files.exists(path)) {
            LOGGER.warning("JSON file not found: " + path.toAbsolutePath());
            return; // Proceed with an empty taskMap if the file doesn't exist
        }

        try {
            String json = Files.readString(path);
            LOGGER.info("JSON content loaded: " + json);

            List<Task> tasks = JsonUtil.fromJson(json, new TypeReference<>() {});
            if (tasks != null && !tasks.isEmpty()) {
                for (Task task : tasks) {
                    taskMap.put(task.getId(), task);
                    idCounter.updateAndGet(current -> Math.max(current, task.getId() + 1));
                }
                LOGGER.info("Loaded " + tasks.size() + " tasks from JSON file.");
            } else {
                LOGGER.warning("No tasks found in the JSON file.");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading JSON file: " + path.toAbsolutePath(), e);
        }
    }

    // Get the next available ID
    @Override
    public int getNextId() {
        return idCounter.getAndIncrement();
    }

    // Save all tasks to the JSON file
    private void saveToJsonFile() {
        try {
            Path path = Paths.get(JSON_FILE_PATH);
            LOGGER.info("Attempting to save tasks to JSON file: " + path.toAbsolutePath());

            String json = JsonUtil.toJson(findAll());
            LOGGER.info("JSON content to save: " + json);

            Files.write(path, json.getBytes());
            LOGGER.info("Tasks successfully saved to JSON file.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving tasks to JSON file.", e);
        }
    }
}
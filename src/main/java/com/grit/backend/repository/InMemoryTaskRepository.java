package com.grit.backend.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.grit.frontend.util.JsonUtil;
import com.grit.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InMemoryTaskRepository implements TaskRepository {
    private static final Logger LOGGER = Logger.getLogger(InMemoryTaskRepository.class.getName());
    private final Map<Integer, Task> taskMap = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    // Path to the JSON file in the resources folder
    private static final String JSON_FILE_PATH = "tasks.json";  // Updated path

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
    public Task save(Task task) {
        if (task.getId() == 0) {
            task.setId(getNextId()); // Auto-generate ID for new tasks
        }
        taskMap.put(task.getId(), task);
        saveToJsonFile();
        return task;
    }

    @Override
    public Task update(Task task) {
        if (!taskMap.containsKey(task.getId())) {
            throw new IllegalArgumentException("Task not found for update");
        }
        taskMap.put(task.getId(), task);
        saveToJsonFile();
        return task;
    }

    @Override
    public boolean deleteById(int id) {
        boolean removed = taskMap.remove(id) != null;
        if (removed) {
            // Optionally, you can adjust the idCounter here if you want to reuse IDs
            saveToJsonFile();
        }
        return removed;
    }

    // Load tasks from JSON file
    private void loadDemoTasksFromJson() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(JSON_FILE_PATH);
        if (inputStream == null) {
            LOGGER.warning("JSON file not found in resources: " + JSON_FILE_PATH);
            LOGGER.warning("Looking in: " + getClass().getClassLoader().getResource(JSON_FILE_PATH));
            return;  // Proceed with an empty taskMap if the file doesn't exist
        }

        try {
            String json = new String(inputStream.readAllBytes());
            List<Task> tasks = JsonUtil.fromJson(json, new TypeReference<>() {});
            if (tasks != null && !tasks.isEmpty()) {
                for (Task task : tasks) {
                    taskMap.put(task.getId(), task);  // Directly populate the taskMap
                    // Update idCounter if the task ID is greater than the current counter
                    idCounter.updateAndGet(current -> Math.max(current, task.getId() + 1));
                }
                LOGGER.info("Loaded " + tasks.size() + " tasks from JSON file.");
            } else {
                LOGGER.warning("No tasks found in the JSON file.");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading JSON file: " + JSON_FILE_PATH, e);
        }
    }

    // Get the next available ID
    private int getNextId() {
        return idCounter.getAndIncrement(); // Increment and return the next ID
    }

    // Save all tasks to the JSON file
    private void saveToJsonFile() {
        try {
            String json = JsonUtil.toJson(findAll());
            Path path = Path.of(getClass().getClassLoader().getResource(JSON_FILE_PATH).toURI());
            Files.write(path, json.getBytes());
            LOGGER.info("Tasks saved to JSON file.");
        } catch (IOException | URISyntaxException e) {
            LOGGER.log(Level.SEVERE, "Error saving tasks to JSON file.", e);
        }
    }
}
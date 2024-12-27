package com.grit.backend.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.grit.frontend.util.JsonUtil;
import com.grit.model.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InMemoryTaskRepository implements TaskRepository {
    private static final Logger LOGGER = Logger.getLogger(InMemoryTaskRepository.class.getName());
    private final AtomicInteger idCounter = new AtomicInteger(1);

    // Path to the tasks.json file in the root project directory
    private static final String JSON_FILE_PATH = System.getProperty("user.dir") + "/tasks.json";

    public InMemoryTaskRepository() {
        initializeIdCounter(); // Initialize the ID counter based on the tasks in the file
    }

    @Override
    public List<Task> findAll() {
        return loadTasksFromJson(); // Always load tasks directly from the file
    }

    @Override
    public Optional<Task> findById(int id) {
        List<Task> tasks = loadTasksFromJson();
        return tasks.stream().filter(task -> task.getId() == id).findFirst();
    }

    @Override
    public synchronized Task save(Task task) {
        List<Task> tasks = loadTasksFromJson();
        if (task.getId() == 0) {
            task.setId(getNextId()); // Auto-generate ID for new tasks
        }
        tasks.add(task);
        saveToJsonFile(tasks); // Save the updated list to the file
        return task;
    }

    @Override
    public synchronized Task update(Task task) {
        List<Task> tasks = loadTasksFromJson();
        Optional<Task> existingTask = tasks.stream().filter(t -> t.getId() == task.getId()).findFirst();
        if (existingTask.isPresent()) {
            Task updatedTask = existingTask.get();
            updatedTask.setDescription(task.getDescription());
            updatedTask.setCompleted(task.isCompleted());
            saveToJsonFile(tasks); // Save the updated list to the file
            return updatedTask;
        } else {
            throw new IllegalArgumentException("Task not found for update");
        }
    }

    @Override
    public synchronized boolean deleteById(int id) {
        List<Task> tasks = loadTasksFromJson();
        boolean removed = tasks.removeIf(task -> task.getId() == id);
        if (removed) {
            saveToJsonFile(tasks); // Save the updated list to the file
        }
        return removed;
    }

    // Load tasks from JSON file
    private List<Task> loadTasksFromJson() {
        Path path = Paths.get(JSON_FILE_PATH);
        LOGGER.info("Attempting to load tasks from JSON file: " + path.toAbsolutePath());

        if (!Files.exists(path)) {
            LOGGER.warning("JSON file not found: " + path.toAbsolutePath());
            return new ArrayList<>(); // Return an empty list if the file doesn't exist
        }

        try {
            String json = Files.readString(path);
            LOGGER.info("JSON content loaded: " + json);

            List<Task> tasks = JsonUtil.fromJson(json, new TypeReference<>() {});
            if (tasks != null) {
                LOGGER.info("Loaded " + tasks.size() + " tasks from JSON file.");
                return tasks;
            } else {
                LOGGER.warning("No tasks found in the JSON file.");
                return new ArrayList<>();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading JSON file: " + path.toAbsolutePath(), e);
            return new ArrayList<>();
        }
    }

    // Save all tasks to the JSON file
    private void saveToJsonFile(List<Task> tasks) {
        try {
            Path path = Paths.get(JSON_FILE_PATH);
            LOGGER.info("Attempting to save tasks to JSON file: " + path.toAbsolutePath());

            String json = JsonUtil.toJson(tasks);
            LOGGER.info("JSON content to save: " + json);

            Files.write(path, json.getBytes());
            LOGGER.info("Tasks successfully saved to JSON file.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving tasks to JSON file.", e);
        }
    }

    // Initialize the ID counter based on the tasks in the file
    private void initializeIdCounter() {
        List<Task> tasks = loadTasksFromJson();
        int maxId = tasks.stream().mapToInt(Task::getId).max().orElse(0);
        idCounter.set(maxId + 1);
    }

    // Get the next available ID
    @Override
    public int getNextId() {
        return idCounter.getAndIncrement();
    }
}
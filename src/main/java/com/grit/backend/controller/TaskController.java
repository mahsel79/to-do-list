package com.grit.backend.controller;

import com.grit.backend.service.TaskService;
import com.grit.model.Task;

import java.util.List;
import java.util.logging.Logger;

public class TaskController {
    private static final Logger LOGGER = Logger.getLogger(TaskController.class.getName());
    private final TaskService taskService;

    // Constructor to initialize TaskController with a TaskService
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
        LOGGER.info("TaskController initialized.");
    }

    // Fetch all tasks
    public List<Task> getAllTasks() {
        LOGGER.info("Fetching all tasks...");
        return taskService.getAllTasks();
    }

    // Fetch a task by its ID
    public Task getTaskById(int id) {
        LOGGER.info("Fetching task with ID: " + id);
        return taskService.findById(id);
    }

    // Create a new task
    public Task createTask(String description, boolean completed, int id) {
        LOGGER.info("Creating new task: " + description);
        Task task = new Task(id, description, completed);
        return taskService.createTask(task);
    }

    // Update an existing task
    public Task updateTask(int id, String description, boolean completed) {
        LOGGER.info("Updating task with ID: " + id);
        return taskService.updateTask(new Task(id, description, completed));
    }

    // Delete a task by its ID
    public boolean deleteTask(int id) {
        LOGGER.info("Deleting task with ID: " + id);
        return taskService.deleteTask(id);
    }

    // Get the next available task ID (based on the current tasks)
    public int getNextTaskId() {
        List<Task> tasks = taskService.getAllTasks();  // Assuming getAllTasks() gets all tasks
        if (tasks == null || tasks.isEmpty()) {
            return 1;  // If there are no tasks, return ID 1 as the next ID
        }

        // Return the next available ID based on the highest existing task ID
        return tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
    }
}

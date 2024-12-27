package com.grit.backend.controller;

import com.grit.backend.exception.TaskNotFoundException;
import com.grit.backend.service.TaskService;
import com.grit.model.Task;
import com.grit.frontend.util.LoggerUtil;

import java.util.List;
import java.util.logging.Logger;

public class TaskController {
    private static final Logger LOGGER = LoggerUtil.getLogger(TaskController.class.getName());
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
        LOGGER.info("TaskController initialized.");
    }

    public List<Task> getAllTasks() {
        LOGGER.info("Fetching all tasks...");
        return taskService.getAllTasks(); // Always fetch the latest tasks
    }

    public Task getTaskById(int id) {
        LOGGER.info("Fetching task with ID: " + id);
        Task task = taskService.findById(id);
        if (task == null) {
            LOGGER.warning("Task with ID: " + id + " not found.");
            throw new TaskNotFoundException("Task not found for ID: " + id);
        }
        return task;
    }

    public Task createTask(String description, boolean completed) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be empty");
        }
        LOGGER.info(() -> "Creating new task: " + description);
        int nextId = taskService.getNextTaskId();
        Task task = new Task(nextId, description, completed);
        return taskService.createTask(task);
    }

    public Task updateTask(int id, String description, boolean completed) {
        LOGGER.info(() -> "Updating task with ID: " + id);
        try {
            Task task = getTaskById(id); // Retrieve existing task
            if (description != null) {
                task.setDescription(description);
            }
            task.setCompleted(completed);
            return taskService.updateTask(task);
        } catch (TaskNotFoundException e) {
            LOGGER.warning("Task with ID: " + id + " not found.");
            throw e; // Re-throw the exception to be handled by the HTTP handler
        }
    }

    public boolean deleteTask(int id) {
        LOGGER.info(() -> "Deleting task with ID: " + id);
        return taskService.deleteTask(id);
    }
}
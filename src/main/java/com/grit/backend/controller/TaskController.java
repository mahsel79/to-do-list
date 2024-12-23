package com.grit.backend.controller;

import com.grit.model.Task;
import java.util.List;
import com.grit.backend.service.TaskService;
import com.grit.backend.service.TaskServiceImpl;
import com.grit.backend.repository.TaskRepository;
import com.grit.backend.repository.InMemoryTaskRepository;


import java.util.Optional;
import java.util.logging.Logger;
import com.grit.frontend.util.LoggerUtil;

public class TaskController {

    private static final Logger logger = LoggerUtil.getLogger(TaskController.class.getName()); // Logger
    private final TaskService taskService;

    // Default constructor for frontend compatibility
    public TaskController() {
        TaskRepository taskRepository = new InMemoryTaskRepository();  // Use your chosen repository implementation
        this.taskService = new TaskServiceImpl(taskRepository);  // Instantiate TaskServiceImpl
        logger.info("TaskController initialized with in-memory task repository.");
    }

    // Constructor with service injection (useful for testing or different service implementations)
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
        logger.info("TaskController initialized with custom task service.");
    }

    // Fetch all tasks
    public List<Task> getAllTasks() {
        logger.info("Fetching all tasks from the service...");
        List<Task> tasks = taskService.getAllTasks();
        logger.info("Fetched " + tasks.size() + " tasks.");
        return tasks;
    }

    // Fetch a task by its ID
    public Optional<Task> getTaskById(int id) {
        logger.info("Fetching task with ID: " + id);
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isPresent()) {
            logger.info("Found task with ID: " + id);
        } else {
            logger.warning("No task found with ID: " + id);
        }
        return task;
    }

    // Create a new task
    public Task createTask(String description, boolean completed) {
        logger.info("Creating a new task with description: " + description + " and completed status: " + completed);
        Task newTask = new Task(0, description, completed);  // Use 0 as the placeholder ID
        Task createdTask = taskService.createTask(newTask);
        logger.info("Created new task with ID: " + createdTask.getId());
        return createdTask;
    }

    // Update an existing task
    public Task updateTask(int id, String description, boolean completed) {
        logger.info("Updating task with ID: " + id + ", new description: " + description + ", new completed status: " + completed);
        Task taskToUpdate = new Task(id, description, completed);
        Task updatedTask = taskService.updateTask(taskToUpdate);
        logger.info("Updated task with ID: " + updatedTask.getId());
        return updatedTask;
    }

    // Delete a task by its ID
    public boolean deleteTask(int id) {
        logger.info("Attempting to delete task with ID: " + id);
        boolean success = taskService.deleteTask(id);
        if (success) {
            logger.info("Task with ID: " + id + " deleted successfully.");
        } else {
            logger.warning("Failed to delete task with ID: " + id);
        }
        return success;
    }
}

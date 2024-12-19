package com.grit.backend.service;

import com.grit.backend.model.Task;
import com.grit.backend.repository.TaskRepository;
import com.grit.frontend.util.LoggerUtil;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class TaskServiceImpl extends TaskService {
    private static final Logger logger = LoggerUtil.getLogger(TaskServiceImpl.class.getName()); // Logger

    // Constructor to initialize TaskRepository
    public TaskServiceImpl(TaskRepository taskRepository) {
        super(taskRepository); // Call the superclass constructor to initialize the repository
        logger.info("TaskServiceImpl initialized.");
    }

    // Get all tasks
    @Override
    public List<Task> getAllTasks() {
        logger.info("Fetching all tasks.");
        List<Task> tasks = taskRepository.findAll(); // Use findAll to retrieve tasks
        logger.info("Total tasks fetched: " + tasks.size());
        return tasks;
    }

    // Get a task by its ID
    @Override
    public Optional<Task> getTaskById(int id) {
        logger.info("Fetching task with ID: " + id);
        Optional<Task> task = taskRepository.findById(id); // Use findById to get task by ID
        if (task.isPresent()) {
            logger.info("Task found: " + task.get().getDescription());
        } else {
            logger.warning("Task with ID " + id + " not found.");
        }
        return task;
    }

    // Create a new task
    @Override
    public Task createTask(Task task) {
        logger.info("Creating task with description: " + task.getDescription());
        Task createdTask = taskRepository.save(task); // Use save to create or update task
        logger.info("Task created with ID: " + createdTask.getId());
        return createdTask;
    }

    // Update an existing task
    @Override
    public Task updateTask(Task task) {
        logger.info("Updating task with ID: " + task.getId());
        Task updatedTask = taskRepository.update(task); // Explicitly update using update method
        logger.info("Task updated with new description: " + updatedTask.getDescription());
        return updatedTask;
    }

    // Delete a task
    @Override
    public boolean deleteTask(int id) {
        logger.info("Deleting task with ID: " + id);
        boolean success = taskRepository.deleteById(id); // Use deleteById to remove the task
        if (success) {
            logger.info("Task with ID " + id + " deleted successfully.");
        } else {
            logger.warning("Failed to delete task with ID " + id);
        }
        return success;
    }
}

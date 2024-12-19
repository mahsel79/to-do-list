package com.grit.backend.service;

import com.grit.backend.model.Task;
import com.grit.backend.repository.TaskRepository;
import com.grit.frontend.util.LoggerUtil;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public abstract class TaskService {
    protected TaskRepository taskRepository;
    private static final Logger logger = LoggerUtil.getLogger(TaskService.class.getName());

    // Constructor to initialize TaskRepository
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        logger.info("TaskService initialized with repository: " + taskRepository.getClass().getSimpleName());
    }

    // Get all tasks
    public List<Task> getAllTasks() {
        logger.info("Fetching all tasks...");
        return taskRepository.findAll();
    }

    // Get a task by its ID
    public Optional<Task> getTaskById(int id) {
        logger.info("Fetching task with ID: " + id);
        return taskRepository.findById(id);
    }

    // Create a new task
    public Task createTask(Task task) {
        logger.info("Creating task: " + task.getDescription());
        return taskRepository.save(task);
    }

    // Update an existing task
    public Task updateTask(Task task) {
        logger.info("Updating task with ID: " + task.getId());
        return taskRepository.update(task);
    }

    // Delete a task
    public boolean deleteTask(int id) {
        logger.info("Deleting task with ID: " + id);
        return taskRepository.deleteById(id);
    }
}

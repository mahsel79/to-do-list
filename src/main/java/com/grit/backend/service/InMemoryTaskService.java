package com.grit.backend.service;

import com.grit.backend.repository.TaskRepository;
import com.grit.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class InMemoryTaskService extends TaskService {

    private static final Logger logger = Logger.getLogger(InMemoryTaskService.class.getName());

    // Constructor to initialize TaskRepository
    public InMemoryTaskService(TaskRepository taskRepository) {
        super(taskRepository); // Call the constructor of the abstract class
    }

    // Get all tasks
    @Override
    public List<Task> getAllTasks() {
        logger.info("Fetching all tasks from repository");
        return taskRepository.findAll(); // Fetch all tasks from repository
    }

    // Get a task by its ID
    @Override
    public Optional<Task> getTaskById(int id) {
        logger.info("Fetching task with ID: " + id);
        return taskRepository.findById(id); // Fetch task by ID from repository
    }

    // Create a new task
    @Override
    public Task createTask(Task task) {
        logger.info("Creating new task: " + task.getDescription());
        return taskRepository.save(task); // Save task in repository
    }

    // Update an existing task
    @Override
    public Task updateTask(Task task) {
        logger.info("Updating task with ID: " + task.getId());
        return taskRepository.update(task); // Update task in repository
    }

    // Delete a task by its ID
    @Override
    public boolean deleteTask(int id) {
        logger.info("Deleting task with ID: " + id);
        return taskRepository.deleteById(id); // Delete task by ID from repository
    }
}

package com.grit.backend.service;

import com.grit.backend.repository.TaskRepository;
import com.grit.model.Task;
import com.grit.backend.exception.TaskNotFoundException;

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
    public Task findById(int id) {
        logger.info("Fetching task with ID: " + id);
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            return taskOptional.get();  // Return the found task
        } else {
            logger.warning("Task with ID " + id + " not found.");
            throw new TaskNotFoundException("Task not found for ID: " + id); // Throw exception if task not found
        }
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

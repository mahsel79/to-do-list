package com.grit.backend.service;

import com.grit.backend.model.Task;
import com.grit.backend.repository.TaskRepository;
import java.util.List;
import java.util.Optional;

public class InMemoryTaskService extends TaskService {

    // Constructor to initialize TaskRepository
    public InMemoryTaskService(TaskRepository taskRepository) {
        super(taskRepository); // Call the constructor of the abstract class
    }

    // Get all tasks
    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll(); // Fetch all tasks from repository
    }

    // Get a task by its ID
    @Override
    public Optional<Task> getTaskById(int id) {
        return taskRepository.findById(id); // Fetch task by ID from repository
    }

    // Create a new task
    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task); // Save task in repository
    }

    // Update an existing task
    @Override
    public Task updateTask(Task task) {
        return taskRepository.update(task); // Update task in repository
    }

    // Delete a task by its ID
    @Override
    public boolean deleteTask(int id) {
        return taskRepository.deleteById(id); // Delete task by ID from repository
    }
}

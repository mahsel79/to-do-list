package com.grit.backend.repository;

import java.util.List;
import java.util.Optional;
import com.grit.model.Task;

public interface TaskRepository {
    List<Task> findAll(); // Retrieve all tasks
    Optional<Task> findById(int id); // Find a task by its ID
    Task save(Task task); // Save or update a task
    Task update(Task task); // Update an existing task
    boolean deleteById(int id); // Delete a task by its ID
    int getNextId(); // Get the next available ID
}
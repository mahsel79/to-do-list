package com.grit.backend.service;

import com.grit.backend.model.Task;
import com.grit.backend.repository.TaskRepository;
import java.util.List;
import java.util.Optional;

public abstract class TaskService {
    protected TaskRepository taskRepository;

    // Constructor to initialize TaskRepository
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Get all tasks
    public abstract List<Task> getAllTasks();

    // Get a task by its ID
    public abstract Optional<Task> getTaskById(int id);

    // Create a new task
    public abstract Task createTask(Task task);

    // Update an existing task
    public abstract Task updateTask(Task task);

    // Delete a task
    public abstract boolean deleteTask(int id);
}

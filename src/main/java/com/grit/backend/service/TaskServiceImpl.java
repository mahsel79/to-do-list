package com.grit.backend.service;

import com.grit.backend.model.Task;
import com.grit.backend.repository.TaskRepository;
import java.util.List;
import java.util.Optional;

public class TaskServiceImpl extends TaskService {

    // Constructor
    public TaskServiceImpl(TaskRepository taskRepository) {
        super(taskRepository); // Call the constructor of the abstract class
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll(); // Fetch all tasks from repository
    }

    @Override
    public Optional<Task> getTaskById(int id) {
        return taskRepository.findById(id); // Fetch task by ID from repository
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task); // Save task in repository
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepository.update(task); // Update task in repository
    }

    @Override
    public boolean deleteTask(int id) {
        return taskRepository.deleteById(id); // Delete task by ID from repository
    }
}

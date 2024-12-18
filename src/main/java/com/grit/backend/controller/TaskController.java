package com.grit.backend.controller;

import com.grit.backend.model.Task;
import com.grit.backend.service.TaskService;
import com.grit.backend.service.TaskServiceImpl;
import com.grit.backend.repository.TaskRepository;
import com.grit.backend.repository.InMemoryTaskRepository;

import java.util.List;
import java.util.Optional;

public class TaskController {
    private final TaskService taskService;

    // Default constructor for frontend compatibility
    public TaskController() {
        TaskRepository taskRepository = new InMemoryTaskRepository();  // Use your chosen repository implementation
        this.taskService = new TaskServiceImpl(taskRepository);  // Instantiate TaskServiceImpl
    }

    // Constructor with service injection (useful for testing or different service implementations)
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Fetch all tasks
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    // Fetch a task by its ID
    public Optional<Task> getTaskById(int id) {
        return taskService.getTaskById(id);
    }

    // Create a new task
    public Task createTask(String description, boolean completed) {
        // Create task with 0 ID (ID will be assigned by repository later)
        Task newTask = new Task(0, description, completed);  // Use 0 as the placeholder ID
        return taskService.createTask(newTask);
    }

    // Update an existing task
    public Task updateTask(int id, String description, boolean completed) {
        // Create task with specified ID
        Task taskToUpdate = new Task(id, description, completed);
        return taskService.updateTask(taskToUpdate);
    }

    // Delete a task by its ID
    public boolean deleteTask(int id) {
        return taskService.deleteTask(id);
    }
}

package com.grit.backend.service;

import com.grit.backend.exception.TaskNotFoundException;
import com.grit.backend.repository.TaskRepository;
import com.grit.model.Task;
import com.grit.frontend.util.LoggerUtil;

import java.util.List;
import java.util.logging.Logger;

public abstract class TaskService {
    protected TaskRepository taskRepository;
    private static final Logger logger = LoggerUtil.getLogger(TaskService.class.getName());

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        logger.info("TaskService initialized with repository: " + taskRepository.getClass().getSimpleName());
    }

    public List<Task> getAllTasks() {
        logger.info("Fetching all tasks...");
        return taskRepository.findAll();
    }

    public abstract Task findById(int id);

    public Task createTask(Task task) {
        logger.info(() -> "Creating task: " + task.getDescription());
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) {
        logger.info(() -> "Updating task with ID: " + task.getId());
        return taskRepository.update(task);
    }

    public boolean deleteTask(int id) {
        logger.info(() -> "Deleting task with ID: " + id);
        return taskRepository.deleteById(id);
    }

    public int getNextTaskId() {
        return taskRepository.getNextId();
    }
}
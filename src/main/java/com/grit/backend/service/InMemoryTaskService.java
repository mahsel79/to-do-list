package com.grit.backend.service;

import com.grit.backend.repository.TaskRepository;
import com.grit.model.Task;
import com.grit.backend.exception.TaskNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class InMemoryTaskService extends TaskService {
    private static final Logger logger = Logger.getLogger(InMemoryTaskService.class.getName());

    public InMemoryTaskService(TaskRepository taskRepository) {
        super(taskRepository);
    }

    @Override
    public List<Task> getAllTasks() {
        logger.info("Fetching all tasks from repository");
        return taskRepository.findAll();
    }

    @Override
    public Task findById(int id) {
        logger.info("Fetching task with ID: " + id);
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            return taskOptional.get();
        } else {
            logger.warning("Task with ID " + id + " not found.");
            throw new TaskNotFoundException("Task not found for ID: " + id);
        }
    }

    @Override
    public Task createTask(Task task) {
        logger.info("Creating new task: " + task.getDescription());
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task) {
        logger.info("Updating task with ID: " + task.getId());
        return taskRepository.update(task);
    }

    @Override
    public boolean deleteTask(int id) {
        logger.info("Deleting task with ID: " + id);
        return taskRepository.deleteById(id);
    }
}
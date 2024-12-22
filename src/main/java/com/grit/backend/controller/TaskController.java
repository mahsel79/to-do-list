package com.grit.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grit.backend.model.Task;
import com.grit.backend.service.TaskService;
import com.grit.backend.service.TaskServiceImpl;
import com.grit.backend.repository.TaskRepository;
import com.grit.backend.repository.InMemoryTaskRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import com.grit.frontend.util.LoggerUtil;

public class TaskController {

    private static final Logger logger = LoggerUtil.getLogger(TaskController.class.getName()); // Logger
    private final TaskService taskService;
    private final ObjectMapper objectMapper;
    private final File tasksFile;

    // Default constructor for frontend compatibility
    public TaskController() {
        TaskRepository taskRepository = new InMemoryTaskRepository();  // Use your chosen repository implementation
        this.taskService = new TaskServiceImpl(taskRepository);  // Instantiate TaskServiceImpl
        this.objectMapper = new ObjectMapper();
        this.tasksFile = new File("backend/tasks.json");
        logger.info("TaskController initialized with in-memory task repository.");
        loadTasksFromFile();
    }

    // Constructor with service injection (useful for testing or different service implementations)
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
        this.objectMapper = new ObjectMapper();
        this.tasksFile = new File("tasks.json");
        logger.info("TaskController initialized with custom task service.");
        loadTasksFromFile();
    }

    // Fetch all tasks
    public List<Task> getAllTasks() {
        logger.info("Fetching all tasks from the service...");
        List<Task> tasks = taskService.getAllTasks();
        logger.info("Fetched " + tasks.size() + " tasks.");
        return tasks;
    }

    // Fetch a task by its ID
    public Optional<Task> getTaskById(int id) {
        logger.info("Fetching task with ID: " + id);
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isPresent()) {
            logger.info("Found task with ID: " + id);
        } else {
            logger.warning("No task found with ID: " + id);
        }
        return task;
    }

    // Create a new task
    public Task createTask(String description, boolean completed) {
        logger.info("Creating a new task with description: " + description + " and completed status: " + completed);
        Task newTask = new Task(0, description, completed);  // Use 0 as the placeholder ID
        Task createdTask = taskService.createTask(newTask);
        logger.info("Created new task with ID: " + createdTask.getId());
        saveTasksToFile();  // Save the updated tasks list to the file
        return createdTask;
    }

    // Update an existing task
    public Task updateTask(int id, String description, boolean completed) {
        Optional<Task> taskOptional = taskService.getTaskById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setDescription(description);
            task.setCompleted(completed);
            taskService.updateTask(task);
            logger.info("Updated task with ID: " + task.getId());
            saveTasksToFile();
            return task;
        } else {
            logger.warning("No task found to update with ID: " + id);
            return null;
        }
    }

    // Delete a task by its ID
    public boolean deleteTask(int id) {
        Optional<Task> taskOptional = taskService.getTaskById(id);
        if (taskOptional.isPresent()) {
            taskService.deleteTask(id);
            logger.info("Deleted task with ID: " + id);
            saveTasksToFile();
            return true;
        } else {
            logger.warning("No task found to delete with ID: " + id);
            return false;
        }
    }

    // Save the tasks to a file
    private void saveTasksToFile() {
        try {
            objectMapper.writeValue(tasksFile, taskService.getAllTasks());
            logger.info("Tasks saved to file successfully.");
        } catch (IOException e) {
            logger.severe("Error saving tasks to file: " + e.getMessage());
        }
    }

    // Load tasks from a file
    private void loadTasksFromFile() {
        try {
            if (tasksFile.exists()) {
                List<Task> tasks = objectMapper.readValue(tasksFile, objectMapper.getTypeFactory().constructCollectionType(List.class, Task.class));
                tasks.forEach(taskService::createTask);
                logger.info("Tasks loaded from file successfully.");
            } else {
                logger.warning("Tasks file not found, starting with empty task list.");
            }
        } catch (IOException e) {
            logger.severe("Error loading tasks from file: " + e.getMessage());
        }
    }
}

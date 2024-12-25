package com.grit.backend.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grit.backend.controller.TaskController;
import com.grit.model.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.logging.Logger;

public class HttpRequestHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(HttpRequestHandler.class.getName());
    private final TaskController taskController;
    private final ObjectMapper objectMapper;

    public HttpRequestHandler(TaskController taskController) {
        this.taskController = taskController;
        this.objectMapper = new ObjectMapper(); // Jackson ObjectMapper to convert objects to JSON
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        if ("GET".equals(requestMethod)) {
            handleGetRequest(exchange);
        } else if ("POST".equals(requestMethod)) {
            handlePostRequest(exchange);
        } else if ("PUT".equals(requestMethod)) {
            handlePutRequest(exchange);
        } else if ("DELETE".equals(requestMethod)) {
            handleDeleteRequest(exchange);
        } else {
            // Handle unsupported methods
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }

    // Handle GET request - Fetch all tasks
    private void handleGetRequest(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskController.getAllTasks();
        String response = objectMapper.writeValueAsString(tasks); // Convert task list to JSON string

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    // Read request body to avoid duplication
    private String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    // Handle POST request - Create new task
    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        Task task = objectMapper.readValue(requestBody, Task.class);

        // Generate a unique ID for the new task
        int nextId = taskController.getNextTaskId();  // Generate task ID
        Task createdTask = taskController.createTask(task.getDescription(), task.isCompleted(), nextId);

        String response = objectMapper.writeValueAsString(createdTask);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, response.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    // Handle PUT request - Update existing task
    private void handlePutRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int taskId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        String requestBody = readRequestBody(exchange);
        Task task = objectMapper.readValue(requestBody, Task.class);
        task.setId(taskId);
        Task updatedTask = taskController.updateTask(taskId, task.getDescription(), task.isCompleted());

        String response = objectMapper.writeValueAsString(updatedTask);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    // Handle DELETE request - Delete task by ID
    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int taskId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));

        boolean isDeleted = taskController.deleteTask(taskId);
        if (isDeleted) {
            exchange.sendResponseHeaders(204, -1); // No content for successful delete
        } else {
            exchange.sendResponseHeaders(404, -1); // Not Found if task not deleted
        }
    }
}
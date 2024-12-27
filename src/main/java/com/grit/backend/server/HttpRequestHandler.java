package com.grit.backend.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grit.backend.controller.TaskController;
import com.grit.backend.exception.TaskNotFoundException;
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

    private static final String TASKS_ENDPOINT = "/tasks";
    private static final String TASK_BY_ID_ENDPOINT = TASKS_ENDPOINT + "/";

    public HttpRequestHandler(TaskController taskController) {
        this.taskController = taskController;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if (path.equals(TASKS_ENDPOINT)) {
                if ("GET".equals(requestMethod)) {
                    handleGetRequest(exchange);
                } else if ("POST".equals(requestMethod)) {
                    handlePostRequest(exchange);
                } else {
                    exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                }
            } else if (path.startsWith(TASK_BY_ID_ENDPOINT)) {
                int taskId = Integer.parseInt(path.substring(TASK_BY_ID_ENDPOINT.length()));
                if ("GET".equals(requestMethod)) {
                    handleGetTaskByIdRequest(exchange, taskId);
                } else if ("PUT".equals(requestMethod)) {
                    handlePutRequest(exchange, taskId);
                } else if ("DELETE".equals(requestMethod)) {
                    handleDeleteRequest(exchange, taskId);
                } else {
                    exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                }
            } else {
                exchange.sendResponseHeaders(404, -1); // Not Found
            }
        } catch (Exception e) {
            LOGGER.severe("Error handling request: " + e.getMessage());
            String response = HttpResponse.createJsonResponse("Internal Server Error", 500);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(500, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskController.getAllTasks();
        String response = objectMapper.writeValueAsString(tasks);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void handleGetTaskByIdRequest(HttpExchange exchange, int taskId) throws IOException {
        try {
            Task task = taskController.getTaskById(taskId);
            String response = objectMapper.writeValueAsString(task);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (TaskNotFoundException e) {
            String response = HttpResponse.createJsonResponse("Task not found", 404);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(404, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        Task task = objectMapper.readValue(requestBody, Task.class);
        Task createdTask = taskController.createTask(task.getDescription(), task.isCompleted());
        String response = objectMapper.writeValueAsString(createdTask);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void handlePutRequest(HttpExchange exchange, int taskId) throws IOException {
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

    private void handleDeleteRequest(HttpExchange exchange, int taskId) throws IOException {
        boolean isDeleted = taskController.deleteTask(taskId);
        String response = HttpResponse.createJsonResponse(isDeleted ? "Task deleted" : "Task not found", isDeleted ? 204 : 404);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(isDeleted ? 204 : 404, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

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
}
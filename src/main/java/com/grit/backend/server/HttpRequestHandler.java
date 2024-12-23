package com.grit.backend.server;

import com.grit.backend.controller.TaskController;
import com.grit.frontend.util.LoggerUtil;
import com.grit.frontend.util.JsonUtil; // Import the JsonUtil
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.grit.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpRequestHandler implements HttpHandler {

    private final TaskController taskController;

    public HttpRequestHandler(TaskController taskController) {
        this.taskController = taskController;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int statusCode = 200;

        String method = exchange.getRequestMethod();
        String requestURI = exchange.getRequestURI().toString();
        LoggerUtil.logInfo("Request method: " + method + " received for " + requestURI);

        try {
            switch (method) {
                case "GET":
                    response = handleGetRequest(exchange);
                    break;
                case "POST":
                    response = handlePostRequest(exchange);
                    break;
                case "PUT":
                    response = handlePutRequest(exchange);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(exchange);
                    break;
                default:
                    statusCode = 405; // Method Not Allowed
                    response = JsonUtil.toJson("Error: Method not allowed.");
                    LoggerUtil.logWarn("Unsupported method: " + method);
            }
        } catch (IllegalArgumentException e) {
            statusCode = 400; // Bad Request
            response = JsonUtil.toJson("Error: " + e.getMessage());
            LoggerUtil.logWarn("Bad request: " + e.getMessage());
        } catch (Exception e) {
            statusCode = 500; // Internal Server Error
            response = JsonUtil.toJson("Server error: " + e.getMessage());
            LoggerUtil.logError("Unhandled exception: " + e.getMessage());
        }

        sendResponse(exchange, statusCode, response);
    }

    private String handleGetRequest(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath().trim(); // Trim whitespace and newlines
        LoggerUtil.logInfo("Received GET request for path: '" + path + "'"); // Log the path
        if (path.equals("/tasks")) {
            LoggerUtil.logInfo("Fetching all tasks...");
            List<Task> tasks = taskController.getAllTasks();
            return JsonUtil.toJson(tasks); // Serialize tasks to JSON
        }
        throw new IllegalArgumentException("Invalid GET path: " + path);
    }

    private String handlePostRequest(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        LoggerUtil.logInfo("Received POST body: " + body);

        String[] parts = body.split(",");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid request body. Expected format: <description>,<completed>");
        }

        String description = parts[0];
        boolean completed = Boolean.parseBoolean(parts[1]);

        Task newTask = taskController.createTask(description, completed);
        LoggerUtil.logInfo("Created new task with ID: " + newTask.getId());
        return JsonUtil.toJson("Task created with ID: " + newTask.getId());
    }

    private String handlePutRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath().trim(); // Trim whitespace and newlines
        String[] segments = path.split("/");
        if (segments.length < 3) {
            throw new IllegalArgumentException("Invalid PUT path. Expected format: /tasks/{id}");
        }

        int taskId = Integer.parseInt(segments[2]);

        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        LoggerUtil.logInfo("Received PUT body: " + body);

        String[] parts = body.split(",");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid request body. Expected format: <description>,<completed>");
        }

        String description = parts[0];
        boolean completed = Boolean.parseBoolean(parts[1]);

        Task updatedTask = taskController.updateTask(taskId, description, completed);
        LoggerUtil.logInfo("Updated task with ID: " + updatedTask.getId());
        return JsonUtil.toJson("Task updated with ID: " + updatedTask.getId());
    }

    private String handleDeleteRequest(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath().trim(); // Trim whitespace and newlines
        String[] segments = path.split("/");
        if (segments.length < 3) {
            throw new IllegalArgumentException("Invalid DELETE path. Expected format: /tasks/{id}");
        }

        int taskId = Integer.parseInt(segments[2]);

        LoggerUtil.logInfo("Received DELETE request for task ID: " + taskId);

        boolean deleted = taskController.deleteTask(taskId);
        if (deleted) {
            LoggerUtil.logInfo("Deleted task with ID: " + taskId);
            return JsonUtil.toJson("Task deleted.");
        } else {
            throw new IllegalArgumentException("Task with ID " + taskId + " not found.");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
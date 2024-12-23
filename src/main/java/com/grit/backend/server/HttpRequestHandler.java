package com.grit.backend.server;

import com.grit.backend.controller.TaskController;
import com.grit.frontend.util.LoggerUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import com.grit.model.Task;

public class HttpRequestHandler implements HttpHandler {

    private final TaskController taskController;

    public HttpRequestHandler(TaskController taskController) {
        this.taskController = taskController;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int statusCode = 200;

        // Get the request method (GET, POST, PUT, DELETE)
        String method = exchange.getRequestMethod();
        LoggerUtil.logInfo("Request method: " + method + " received for " + exchange.getRequestURI().getPath());

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
                    response = "Method not allowed";
                    LoggerUtil.logWarn("Method not allowed: " + method);
            }
        } catch (Exception e) {
            statusCode = 500; // Internal Server Error
            response = "Server error: " + e.getMessage();
            LoggerUtil.logError("Error processing request: " + e.getMessage());
        }

        // Send response headers
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    private String handleGetRequest(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/tasks")) {
            LoggerUtil.logInfo("Fetching all tasks");
            List<Task> tasks = taskController.getAllTasks();
            StringBuilder sb = new StringBuilder();
            for (Task task : tasks) {
                sb.append("ID: ").append(task.getId()).append(", ")
                        .append("Description: ").append(task.getDescription()).append(", ")
                        .append("Completed: ").append(task.isCompleted()).append("\n");
            }
            LoggerUtil.logInfo("Returning " + tasks.size() + " tasks");
            return sb.toString();
        }
        LoggerUtil.logWarn("Invalid path for GET request: " + path);
        return "Invalid path";
    }

    private String handlePostRequest(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        LoggerUtil.logInfo("Received POST request body: " + body);

        String[] parts = body.split(",");
        String description = parts[0];
        boolean completed = Boolean.parseBoolean(parts[1]);

        Task newTask = taskController.createTask(description, completed);
        LoggerUtil.logInfo("Created new task with ID: " + newTask.getId());

        return "Task created with ID: " + newTask.getId();
    }

    private String handlePutRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int taskId = Integer.parseInt(path.split("/")[2]);

        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        LoggerUtil.logInfo("Received PUT request for task ID: " + taskId + " with body: " + body);

        String[] parts = body.split(",");
        String description = parts[0];
        boolean completed = Boolean.parseBoolean(parts[1]);

        Task updatedTask = taskController.updateTask(taskId, description, completed);
        LoggerUtil.logInfo("Updated task with ID: " + updatedTask.getId());

        return "Task updated with ID: " + updatedTask.getId();
    }

    private String handleDeleteRequest(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        int taskId = Integer.parseInt(path.split("/")[2]);

        LoggerUtil.logInfo("Received DELETE request for task ID: " + taskId);

        boolean deleted = taskController.deleteTask(taskId);
        if (deleted) {
            LoggerUtil.logInfo("Task with ID: " + taskId + " deleted successfully");
            return "Task deleted";
        } else {
            LoggerUtil.logWarn("Task with ID: " + taskId + " not found for deletion");
            return "Task not found";
        }
    }
}

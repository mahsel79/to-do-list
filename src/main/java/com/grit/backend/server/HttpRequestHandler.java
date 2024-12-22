package com.grit.backend.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grit.backend.controller.TaskController;
import com.grit.backend.model.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import com.grit.frontend.util.LoggerUtil;
import com.grit.frontend.util.TaskEventManager;

public class HttpRequestHandler implements HttpHandler {
    private final TaskController taskController;
    private final TaskEventManager eventManager = TaskEventManager.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final int MAX_REQUEST_SIZE = 1024 * 1024; // 1MB limit

    public HttpRequestHandler(TaskController taskController) {
        this.taskController = taskController;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // Set CORS headers
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            // Handle OPTIONS requests for CORS
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            // Validate content type for POST/PUT requests
            if ("POST".equals(exchange.getRequestMethod()) || "PUT".equals(exchange.getRequestMethod())) {
                String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
                if (contentType == null || !contentType.contains("application/json")) {
                    sendErrorResponse(exchange, "Content-Type must be application/json", 415);
                    return;
                }
            }

            switch (exchange.getRequestMethod()) {
                case "GET":
                    handleGetRequest(exchange);
                    break;
                case "POST":
                    handlePostRequest(exchange);
                    break;
                case "PUT":
                    handlePutRequest(exchange);
                    break;
                case "DELETE":
                    handleDeleteRequest(exchange);
                    break;
                default:
                    sendErrorResponse(exchange, "Method not allowed", 405);
            }
        } catch (Exception e) {
            LoggerUtil.logError("Error processing request: " + e.getMessage());
            sendErrorResponse(exchange, "Internal server error", 500);
        }
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody();
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            int totalBytes = 0;

            while ((bytesRead = is.read(buffer)) != -1) {
                totalBytes += bytesRead;
                if (totalBytes > MAX_REQUEST_SIZE) {
                    throw new IOException("Request body too large");
                }
                os.write(buffer, 0, bytesRead);
            }

            return os.toString(StandardCharsets.UTF_8);
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        try {
            Map requestData = objectMapper.readValue(body, Map.class);
            String description = (String) requestData.get("description");
            Boolean completed = (Boolean) requestData.get("completed");

            if (description == null || description.trim().isEmpty()) {
                sendErrorResponse(exchange, "Description is required", 400);
                return;
            }

            Task newTask = taskController.createTask(description, completed != null ? completed : false);
            eventManager.notifyTaskChange("CREATE");

            sendJsonResponse(exchange, objectMapper.writeValueAsString(newTask), 201);
        } catch (Exception e) {
            sendErrorResponse(exchange, "Invalid request format: " + e.getMessage(), 400);
        }
    }

    private void handlePutRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");

        if (pathParts.length != 3) {
            sendErrorResponse(exchange, "Invalid URL format", 400);
            return;
        }

        try {
            int taskId = Integer.parseInt(pathParts[2]);
            String body = readRequestBody(exchange);
            Map<String, Object> requestData = objectMapper.readValue(body, Map.class);

            String description = (String) requestData.get("description");
            Boolean completed = (Boolean) requestData.get("completed");

            if (description == null || description.trim().isEmpty()) {
                sendErrorResponse(exchange, "Description is required", 400);
                return;
            }

            Task updatedTask = taskController.updateTask(taskId, description, completed != null ? completed : false);
            if (updatedTask != null) {
                eventManager.notifyTaskChange("UPDATE");
                sendJsonResponse(exchange, objectMapper.writeValueAsString(updatedTask), 200);
            } else {
                sendErrorResponse(exchange, "Task not found", 404);
            }
        } catch (NumberFormatException e) {
            sendErrorResponse(exchange, "Invalid task ID format", 400);
        } catch (Exception e) {
            sendErrorResponse(exchange, "Invalid request format: " + e.getMessage(), 400);
        }
    }

    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        if (pathParts.length != 3) {
            sendErrorResponse(exchange, "Invalid URL format", 400);
            return;
        }

        try {
            int taskId = Integer.parseInt(pathParts[2]);
            boolean deleted = taskController.deleteTask(taskId);

            if (deleted) {
                eventManager.notifyTaskChange("DELETE");
                sendJsonResponse(exchange, "{\"message\":\"Task deleted successfully\"}", 200);
            } else {
                sendErrorResponse(exchange, "Task not found", 404);
            }
        } catch (NumberFormatException e) {
            sendErrorResponse(exchange, "Invalid task ID format", 400);
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskController.getAllTasks();
        sendJsonResponse(exchange, objectMapper.writeValueAsString(tasks), 200);
    }

    private void sendJsonResponse(HttpExchange exchange, String jsonResponse, int statusCode) throws IOException {
        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    private void sendErrorResponse(HttpExchange exchange, String message, int statusCode) throws IOException {
        String jsonError = String.format("{\"error\":\"%s\"}", message);
        sendJsonResponse(exchange, jsonError, statusCode);
    }
}
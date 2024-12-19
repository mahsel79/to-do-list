package com.grit.backend.server;

import com.grit.backend.controller.TaskController;
import com.grit.backend.model.Task;
import com.grit.backend.service.TaskServiceImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
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

        switch (exchange.getRequestMethod()) {
            case "GET":
                response = handleGetTasks();
                break;
            case "POST":
                response = handleCreateTask(exchange);
                break;
            case "PUT":
                response = handleUpdateTask(exchange);
                break;
            case "DELETE":
                response = handleDeleteTask(exchange);
                break;
            default:
                statusCode = 405; // Method Not Allowed
                response = "Method not allowed";
                break;
        }

        // Send response
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String handleGetTasks() {
        List<Task> tasks = taskController.getAllTasks();
        return tasks.toString();  // Simple response: Convert list of tasks to string
    }

    private String handleCreateTask(HttpExchange exchange) throws IOException {
        // Get the task description from the request body
        String description = new String(exchange.getRequestBody().readAllBytes());
        Task newTask = taskController.createTask(description, false);  // Default completed = false
        return "Task created: " + newTask.getDescription();
    }

    private String handleUpdateTask(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String taskId = uri.getPath().split("/")[2];  // Extract task ID from URL path
        String description = new String(exchange.getRequestBody().readAllBytes());

        Task updatedTask = taskController.updateTask(Integer.parseInt(taskId), description, false);
        return "Task updated: " + updatedTask.getDescription();
    }

    private String handleDeleteTask(HttpExchange exchange) {
        URI uri = exchange.getRequestURI();
        String taskId = uri.getPath().split("/")[2];  // Extract task ID from URL path
        taskController.deleteTask(Integer.parseInt(taskId));
        return "Task deleted successfully";
    }
}

package com.grit.backend.server;

import com.grit.backend.controller.TaskController;
import com.grit.backend.repository.InMemoryTaskRepository;
import com.grit.backend.repository.TaskRepository;
import com.grit.backend.service.InMemoryTaskService;
import com.grit.backend.service.TaskService;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskHttpServer {

    private static final int PORT = 8080;
    private static final Logger LOGGER = Logger.getLogger(TaskHttpServer.class.getName());
    private HttpServer server;

    public void start() {
        try {
            TaskRepository taskRepository = new InMemoryTaskRepository();
            TaskService taskService = new InMemoryTaskService(taskRepository);
            TaskController taskController = new TaskController(taskService);

            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/tasks", new HttpRequestHandler(taskController));

            server.setExecutor(null);
            server.start();

            LOGGER.info("Server started on port " + PORT);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to start the HTTP server", e);
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            LOGGER.info("Server stopped successfully.");
        }
    }

    public static void main(String[] args) {
        TaskHttpServer httpServer = new TaskHttpServer();
        httpServer.start();
    }
}
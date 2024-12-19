package com.grit.backend.server;

import com.grit.backend.controller.TaskController;
import com.grit.backend.repository.InMemoryTaskRepository;
import com.grit.backend.service.TaskServiceImpl;
import com.grit.backend.service.TaskService;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class TaskHttpServer {

    private static final int PORT = 8080;
    private HttpServer server;

    public void start() throws IOException {
        // Create a task controller
        TaskController taskController = new TaskController(new TaskServiceImpl(new InMemoryTaskRepository()));

        // Create a new HttpServer
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new HttpRequestHandler(taskController)); // Handle tasks route
        server.setExecutor(null); // Use the default executor
        server.start();

        System.out.println("Server started on port " + PORT);
    }

    public void stop() {
        server.stop(0); // Stop the server gracefully
    }

    public static void main(String[] args) throws IOException {
        TaskHttpServer httpServer = new TaskHttpServer();
        httpServer.start();
    }
}

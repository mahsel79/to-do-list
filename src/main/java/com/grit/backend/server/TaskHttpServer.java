package com.grit.backend.server;

import com.grit.backend.controller.TaskController;
import com.grit.backend.repository.InMemoryTaskRepository;
import com.grit.backend.service.TaskServiceImpl;
import com.grit.backend.service.TaskDataInitializer;
import com.grit.frontend.util.LoggerUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.sun.net.httpserver.HttpServer;

public class TaskHttpServer {
    private static final Logger logger = LoggerUtil.getLogger(TaskHttpServer.class.getName());
    private static final int PORT = 8080;
    private HttpServer server;
    private final TaskController taskController;

    public TaskHttpServer() {
        // Create a shared TaskController instance with its dependencies
        this.taskController = new TaskController(new TaskServiceImpl(new InMemoryTaskRepository()));
        // Initialize demo data
        TaskDataInitializer.initializeData(this.taskController);
        logger.info("TaskHttpServer initialized with shared TaskController");
    }

    public TaskHttpServer(TaskController taskController) {
        // Constructor that accepts an existing TaskController
        this.taskController = taskController;
        logger.info("TaskHttpServer initialized with provided TaskController");
    }

    public void start() throws IOException {
        try {
            // Create a new HttpServer
            server = HttpServer.create(new InetSocketAddress(PORT), 0);

            // Create context with the shared TaskController
            server.createContext("/tasks", new HttpRequestHandler(taskController));

            // Configure and start the server
            configureServer();
            server.start();

            logger.info("Server started successfully on port " + PORT);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to start server: " + e.getMessage(), e);
            throw e;
        }
    }

    public void stop() {
        if (server != null) {
            try {
                server.stop(0); // Stop the server gracefully
                logger.info("Server stopped gracefully");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error stopping server: " + e.getMessage(), e);
            }
        }
    }

    private void configureServer() {
        // Set a fixed thread pool executor with 10 threads for handling requests
        server.setExecutor(Executors.newFixedThreadPool(10));
        logger.info("Server configured with fixed thread pool of 10 threads");

        // Add a shutdown hook to gracefully stop the server on JVM termination
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutdown hook triggered, stopping server...");
            stop();
        }));
    }

    // Getter for TaskController (useful for testing and verification)
    public TaskController getTaskController() {
        return taskController;
    }

    public static void main(String[] args) {
        try {
            TaskHttpServer httpServer = new TaskHttpServer();
            httpServer.start();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to start server in main: " + e.getMessage(), e);
            System.exit(1);
        }
    }
}
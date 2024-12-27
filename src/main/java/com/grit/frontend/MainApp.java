package com.grit.frontend;

import com.grit.backend.server.TaskHttpServer;
import com.grit.frontend.util.LoggerUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainApp extends Application {
    private static final Logger logger = LoggerUtil.getLogger(MainApp.class.getName());
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private TaskHttpServer server;

    @Override
    public void start(Stage stage) {
        logger.info("Starting the application...");

        // Initialize JavaFX UI first
        initializeUI(stage);

        // Start the HTTP server in a separate thread after UI is initialized
        executorService.execute(this::startHttpServer);
    }

    private void initializeUI(Stage stage) {
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/grit/frontend/main-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 400);
                stage.setTitle("Reminder!");
                stage.setScene(scene);
                stage.show();

                logger.info("JavaFX application started successfully.");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to load the FXML file: main-view.fxml. Please check the file's existence and format.", e);
                shutdownApplication("Failed to load the UI.");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Unexpected error occurred while starting the JavaFX application.", e);
                shutdownApplication("Unexpected error during application startup.");
            }
        });
    }

    private void startHttpServer() {
        try {
            server = new TaskHttpServer();
            server.start();
            logger.info("HTTP server started successfully.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to start the HTTP server", e);
            shutdownApplication("Failed to start the HTTP server.");
        }
    }

    private void shutdownApplication(String reason) {
        logger.warning("Shutting down application due to error: " + reason);
        stopServer();
        Platform.exit();
        System.exit(1);
    }

    private void stopServer() {
        try {
            if (server != null) {
                server.stop();
                logger.info("HTTP server stopped successfully.");
            }
            executorService.shutdownNow();
            logger.info("HTTP server thread terminated.");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to stop the HTTP server gracefully.", e);
        }
    }

    public static void main(String[] args) {
        logger.info("Launching the application...");
        try {
            launch();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Critical error occurred during application launch.", e);
        } finally {
            logger.info("Application has been terminated.");
        }
    }
}
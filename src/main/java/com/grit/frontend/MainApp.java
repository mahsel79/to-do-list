package com.grit.frontend;

import com.grit.backend.controller.TaskController;
import com.grit.backend.repository.InMemoryTaskRepository;
import com.grit.backend.server.TaskHttpServer;
import com.grit.backend.service.TaskServiceImpl;
import com.grit.frontend.controller.ToDoController;
import com.grit.frontend.util.LoggerUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.grit.backend.service.TaskDataInitializer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainApp extends Application {
    private static final Logger logger = LoggerUtil.getLogger(MainApp.class.getName());
    private static TaskController sharedTaskController;

    @Override
    public void start(Stage stage) {
        logger.info("Starting the application...");

        // Create shared TaskController
        sharedTaskController = new TaskController(new TaskServiceImpl(new InMemoryTaskRepository()));

        // Initialize demo data
        TaskDataInitializer.initializeData(sharedTaskController);

        // Start the server in a separate thread
        new Thread(() -> {
            try {
                TaskHttpServer server = new TaskHttpServer();
                server.start();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to start the HTTP server", e);
            }
        }).start();

        // Start JavaFX UI
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/grit/frontend/main-view.fxml"));

                // Set controller factory to use shared TaskController
                fxmlLoader.setControllerFactory(param -> {
                    if (param == ToDoController.class) {
                        return new ToDoController(sharedTaskController);
                    }
                    return null;
                });

                Scene scene = new Scene(fxmlLoader.load(), 500, 400);
                stage.setTitle("Reminder!");
                stage.setScene(scene);
                stage.show();
                logger.info("Application started successfully.");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to load the FXML file: main-view.fxml. Please check the file's existence and format.", e);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Unexpected error occurred while starting the application.", e);
            }
        });
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

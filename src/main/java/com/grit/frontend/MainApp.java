package com.grit.frontend;

import com.grit.frontend.util.LoggerUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainApp extends Application {
    private static final Logger logger = LoggerUtil.getLogger(MainApp.class.getName());

    @Override
    public void start(Stage stage) {
        logger.info("Starting the application...");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/grit/frontend/main-view.fxml"));
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

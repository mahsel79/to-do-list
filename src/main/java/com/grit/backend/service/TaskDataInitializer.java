package com.grit.backend.service;

import com.grit.backend.controller.TaskController;
import com.grit.frontend.util.LoggerUtil;
import java.util.logging.Logger;

public class TaskDataInitializer {
    private static final Logger logger = LoggerUtil.getLogger(TaskDataInitializer.class.getName());
    private static boolean initialized = false;
    private static final Object lock = new Object();

    public static void initializeData(TaskController taskController) {
        synchronized (lock) {
            if (!initialized) {
                logger.info("Initializing demo data...");
                if (taskController.getAllTasks().isEmpty()) {
                    // Add demo tasks
                    taskController.createTask("Finish JavaFX tutorial", false);
                    taskController.createTask("Buy groceries", false);
                    taskController.createTask("Clean the house", true);
                    logger.info("Demo data added successfully.");
                } else {
                    logger.info("Data already exists, skipping demo data initialization.");
                }
                initialized = true;
            }
        }
    }

    public static void resetInitialization() {
        synchronized (lock) {
            initialized = false;
        }
    }
}
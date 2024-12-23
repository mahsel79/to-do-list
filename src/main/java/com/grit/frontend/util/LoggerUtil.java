package com.grit.frontend.util;

import java.io.IOException;
import java.util.logging.*;

public class LoggerUtil {

    private static final Logger logger;

    static {
        logger = Logger.getLogger(LoggerUtil.class.getName());

        try {
            // Create a file handler that writes log to the 'logs/app.log' file
            FileHandler fileHandler = new FileHandler("logs/app.log", true);
            fileHandler.setFormatter(new SimpleFormatter());

            // Add file handler to the logger
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false); // Disable console output
            logger.setLevel(Level.ALL); // Set logging level to capture all logs
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    // Public method to retrieve the logger
    public static Logger getLogger(String name) {
        return logger;
    }

    // Method to log info level messages
    public static void logInfo(String message) {
        logger.log(Level.INFO, message);
    }

    // Method to log warning level messages
    public static void logWarn(String message) {
        logger.log(Level.WARNING, message);
    }

    // Method to log error level messages
    public static void logError(String message) {
        logger.log(Level.SEVERE, message);
    }
}

package com.grit.frontend.util;

import java.io.IOException;
import java.util.logging.*;

public class LoggerUtil {

    private static FileHandler fileHandler;

    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        try {
            if (fileHandler == null) {
                fileHandler = new FileHandler("logs/app.log", true);
                fileHandler.setFormatter(new SimpleFormatter());
                logger.addHandler(fileHandler);
            }
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger for " + name + ": " + e.getMessage());
        }
        return logger;
    }
}
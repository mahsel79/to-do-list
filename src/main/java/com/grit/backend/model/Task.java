package com.grit.backend.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.logging.Logger;
import com.grit.frontend.util.LoggerUtil;

public class Task {
    private static final Logger logger = LoggerUtil.getLogger(Task.class.getName()); // Logger

    private final IntegerProperty id;
    private final StringProperty description;
    private final BooleanProperty completed;

    // Constructor with properties
    public Task(int id, String description, boolean completed) {
        this.id = new SimpleIntegerProperty(id);
        this.description = new SimpleStringProperty(description);
        this.completed = new SimpleBooleanProperty(completed);
        logger.info("Task created with ID: " + id + ", Description: " + description + ", Completed: " + completed);
    }

    // Getters for properties
    public int getId() {
        int taskId = id.get();
        logger.fine("Getting ID of task: " + taskId);
        return taskId;
    }

    public String getDescription() {
        String taskDescription = description.get();
        logger.fine("Getting description of task: " + taskDescription);
        return taskDescription;
    }

    public boolean isCompleted() {
        boolean taskCompleted = completed.get();
        logger.fine("Getting completion status of task: " + taskCompleted);
        return taskCompleted;
    }

    // Setters for properties
    public void setId(int id) {
        this.id.set(id);
        logger.info("Setting ID of task to: " + id);
    }

    public void setDescription(String description) {
        this.description.set(description);
        logger.info("Setting description of task to: " + description);
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
        logger.info("Setting completion status of task to: " + completed);
    }

    // Property accessors for JavaFX TableView binding
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public BooleanProperty completedProperty() {
        return completed;
    }
}

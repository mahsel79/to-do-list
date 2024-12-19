package com.grit.frontend.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.grit.frontend.util.LoggerUtil;
import java.util.logging.Logger;

public class Task {

    private static final Logger logger = LoggerUtil.getLogger(Task.class.getName()); // Add logger
    private final IntegerProperty id;
    private final StringProperty description;
    private final BooleanProperty completed;

    // Constructor: log when a task is created
    public Task(int id, String description, boolean completed) {
        this.id = new SimpleIntegerProperty(id);
        this.description = new SimpleStringProperty(description);
        this.completed = new SimpleBooleanProperty(completed);
        logger.info("Task created: ID=" + id + ", Description=" + description + ", Completed=" + completed); // Log task creation
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public BooleanProperty completedProperty() {
        return completed;
    }

    public int getId() {
        logger.fine("Accessing ID of Task: " + id.get()); // Log access to ID
        return id.get();
    }

    public String getDescription() {
        logger.fine("Accessing Description of Task: " + description.get()); // Log access to Description
        return description.get();
    }

    public boolean isCompleted() {
        logger.fine("Accessing Completed status of Task: " + completed.get()); // Log access to Completed status
        return completed.get();
    }

    // Setters with logging
    public void setDescription(String description) {
        logger.info("Setting new Description for Task ID " + id.get() + ": " + description); // Log description change
        this.description.set(description);
    }

    public void setCompleted(boolean completed) {
        logger.info("Setting new Completed status for Task ID " + id.get() + ": " + completed); // Log completion status change
        this.completed.set(completed);
    }
}

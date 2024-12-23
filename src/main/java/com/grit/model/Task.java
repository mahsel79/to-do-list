package com.grit.model;

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

    private final IntegerProperty id;           // For JavaFX TableView binding
    private final StringProperty description;   // For JavaFX TableView binding
    private final BooleanProperty completed;    // For JavaFX TableView binding

    // Constructor
    public Task(int id, String description, boolean completed) {
        this.id = new SimpleIntegerProperty(id);
        this.description = new SimpleStringProperty(description);
        this.completed = new SimpleBooleanProperty(completed);
        logger.info("Task created: ID=" + id + ", Description=" + description + ", Completed=" + completed);
    }

    // JavaFX Property Accessors (used in TableView binding and frontend UI)
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public BooleanProperty completedProperty() {
        return completed;
    }

    // Getters (standard Java accessors)
    public int getId() {
        logger.fine(() -> "Accessing ID: " + id.get());
        return id.get();
    }

    public String getDescription() {
        logger.fine(() -> "Accessing Description: " + description.get());
        return description.get();
    }

    public boolean isCompleted() {
        logger.fine(() -> "Accessing Completed status: " + completed.get());
        return completed.get();
    }

    // Setters (with logging)
    public void setId(int id) {
        this.id.set(id);
        logger.info(() -> "Setting ID to: " + id);
    }

    public void setDescription(String description) {
        this.description.set(description);
        logger.info(() -> "Setting Description to: " + description);
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
        logger.info(() -> "Setting Completed status to: " + completed);
    }
}

package com.grit.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.logging.Logger;
import com.grit.frontend.util.LoggerUtil;

public class Task {
    private static final Logger logger = LoggerUtil.getLogger(Task.class.getName());

    private final IntegerProperty id;
    private final StringProperty description;
    private final BooleanProperty completed;

    public Task() {
        this.id = new SimpleIntegerProperty();
        this.description = new SimpleStringProperty();
        this.completed = new SimpleBooleanProperty();
        logger.info("Task object created using default constructor.");
    }

    public Task(int id, String description, boolean completed) {
        this.id = new SimpleIntegerProperty(id);
        this.description = new SimpleStringProperty(description);
        this.completed = new SimpleBooleanProperty(completed);
        logger.info(() -> "Task created: ID=" + id + ", Description=" + description + ", Completed=" + completed);
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

    @JsonProperty("id")
    public int getId() {
        return id.get();
    }

    @JsonProperty("description")
    public String getDescription() {
        return description.get();
    }

    @JsonProperty("completed")
    public boolean isCompleted() {
        return completed.get();
    }

    public void setId(int id) {
        this.id.set(id);
        logger.info("Setting ID to: " + id);
    }

    public void setDescription(String description) {
        this.description.set(description);
        logger.info("Setting Description to: " + description);
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
        logger.info("Setting Completed status to: " + completed);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id.get() +
                ", description='" + description.get() + '\'' +
                ", completed=" + completed.get() +
                '}';
    }
}
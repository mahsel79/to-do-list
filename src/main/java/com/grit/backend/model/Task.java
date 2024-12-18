package com.grit.backend.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task {
    private final IntegerProperty id;
    private final StringProperty description;
    private final BooleanProperty completed;

    // Constructor with properties
    public Task(int id, String description, boolean completed) {
        this.id = new SimpleIntegerProperty(id);
        this.description = new SimpleStringProperty(description);
        this.completed = new SimpleBooleanProperty(completed);
    }

    // Getters for properties
    public int getId() {
        return id.get();
    }

    public String getDescription() {
        return description.get();
    }

    public boolean isCompleted() {
        return completed.get();
    }

    // Setters for properties
    public void setId(int id) {
        this.id.set(id);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
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

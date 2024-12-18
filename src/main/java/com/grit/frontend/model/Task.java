package com.grit.frontend.model;

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

    public Task(int id, String description, boolean completed) {
        this.id = new SimpleIntegerProperty(id);
        this.description = new SimpleStringProperty(description);
        this.completed = new SimpleBooleanProperty(completed);
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
        return id.get();
    }

    public String getDescription() {
        return description.get();
    }

    public boolean isCompleted() {
        return completed.get();
    }
}

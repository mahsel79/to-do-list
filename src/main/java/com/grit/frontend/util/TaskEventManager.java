package com.grit.frontend.util;

import com.grit.backend.model.Task;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class TaskEventManager {
    private static TaskEventManager instance;
    private final List<Consumer<String>> listeners = new CopyOnWriteArrayList<>();

    private TaskEventManager() {}

    public static TaskEventManager getInstance() {
        if (instance == null) {
            instance = new TaskEventManager();
        }
        return instance;
    }

    public void addListener(Consumer<String> listener) {
        listeners.add(listener);
    }

    public void notifyTaskChange(String event) {
        Platform.runLater(() -> {
            for (Consumer<String> listener : listeners) {
                listener.accept(event);
            }
        });
    }
}

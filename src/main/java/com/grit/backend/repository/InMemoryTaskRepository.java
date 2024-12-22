package com.grit.backend.repository;

import com.grit.backend.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InMemoryTaskRepository implements TaskRepository {
    private final List<Task> tasks = Collections.synchronizedList(new ArrayList<>()); // Thread-safe list
    private int idCounter = 1; // To generate unique IDs

    @Override
    public synchronized List<Task> findAll() {
        return new ArrayList<>(tasks); // Return a copy to prevent concurrent modification
    }

    @Override
    public synchronized Optional<Task> findById(int id) {
        return tasks.stream().filter(task -> task.getId() == id).findFirst(); // Find task by ID
    }

    @Override
    public synchronized Task save(Task task) {
        if (task.getId() == 0) { // If task doesn't have an ID, set a new one
            task.setId(idCounter++);
            tasks.add(task);
        } else {
            update(task); // If task has an ID, update it
        }
        return task;
    }

    @Override
    public synchronized Task update(Task task) {
        Optional<Task> existingTask = findById(task.getId());
        if (existingTask.isPresent()) {
            Task t = existingTask.get();
            t.setDescription(task.getDescription());
            t.setCompleted(task.isCompleted());
        }
        return task;
    }

    @Override
    public synchronized boolean deleteById(int id) {
        return tasks.removeIf(task -> task.getId() == id); // Remove task by ID
    }
}

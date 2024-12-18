package com.grit.backend.repository;

import com.grit.backend.model.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryTaskRepository implements TaskRepository {
    private List<Task> tasks = new ArrayList<>(); // Store tasks in memory
    private int idCounter = 1; // To generate unique IDs

    @Override
    public List<Task> findAll() {
        return tasks; // Return all tasks
    }

    @Override
    public Optional<Task> findById(int id) {
        return tasks.stream().filter(task -> task.getId() == id).findFirst(); // Find task by ID
    }

    @Override
    public Task save(Task task) {
        if (task.getId() == 0) { // If task doesn't have an ID, set a new one
            task.setId(idCounter++);
            tasks.add(task);
        } else {
            update(task); // If task has an ID, update it
        }
        return task;
    }

    @Override
    public Task update(Task task) {
        Optional<Task> existingTask = findById(task.getId());
        if (existingTask.isPresent()) {
            Task t = existingTask.get();
            t.setDescription(task.getDescription());
            t.setCompleted(task.isCompleted());
        }
        return task;
    }

    @Override
    public boolean deleteById(int id) {
        return tasks.removeIf(task -> task.getId() == id); // Remove task by ID
    }
}

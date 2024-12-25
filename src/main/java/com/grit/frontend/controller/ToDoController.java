package com.grit.frontend.controller;

import com.grit.backend.controller.TaskController;
import com.grit.backend.repository.InMemoryTaskRepository;
import com.grit.backend.service.InMemoryTaskService;
import com.grit.backend.service.TaskService;
import com.grit.model.Task;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.logging.Logger;

public class ToDoController {
    private static final Logger LOGGER = Logger.getLogger(ToDoController.class.getName());

    @FXML
    private TableView<Task> taskTableView;
    @FXML
    private TableColumn<Task, Integer> idColumn;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TableColumn<Task, Boolean> completedColumn;
    @FXML
    private TextField descriptionField;
    @FXML
    private CheckBox checkBox;
    @FXML
    private Button saveButton;

    private final TaskController taskController;
    private Task selectedTask;

    public ToDoController() {
        // Pass the TaskService instance to the TaskController constructor
        TaskService taskService = new InMemoryTaskService(new InMemoryTaskRepository());
        this.taskController = new TaskController(taskService); // Provide the TaskService
    }

    @FXML
    public void initialize() {
        LOGGER.info("Initializing ToDoController...");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        completedColumn.setCellValueFactory(cellData -> cellData.getValue().completedProperty().asObject());
        loadTasksFromBackend();
    }

    private void loadTasksFromBackend() {
        List<Task> tasks = taskController.getAllTasks();
        if (tasks != null && !tasks.isEmpty()) {
            taskTableView.setItems(FXCollections.observableArrayList(tasks));
            LOGGER.info("Loaded " + tasks.size() + " tasks.");
        } else {
            LOGGER.warning("No tasks available.");
        }
    }

    @FXML
    public void handleAddTask() {
        String description = descriptionField.getText();
        if (description.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Description cannot be empty.");
            return;
        }

        boolean isCompleted = checkBox.isSelected();
        // Generate a unique ID for the new task
        int nextId = taskController.getNextTaskId();
        taskController.createTask(description, isCompleted, nextId);  // Pass the ID
        loadTasksFromBackend();
        clearForm();
    }


    @FXML
    public void handleEditTask() {
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "Select a task to edit.");
            return;
        }
        descriptionField.setText(selectedTask.getDescription());
        checkBox.setSelected(selectedTask.isCompleted());
        saveButton.setVisible(true);
    }

    @FXML
    public void handleSaveTask() {
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "No task selected to save.");
            return;
        }

        String description = descriptionField.getText();
        if (description.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Description cannot be empty.");
            return;
        }

        taskController.updateTask(selectedTask.getId(), description, checkBox.isSelected());
        loadTasksFromBackend();
        clearForm();
    }

    @FXML
    public void handleDeleteTask() {
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "No task selected to delete.");
            return;
        }

        taskController.deleteTask(selectedTask.getId());
        loadTasksFromBackend();
    }

    private void clearForm() {
        descriptionField.clear();
        checkBox.setSelected(false);
        saveButton.setVisible(false);
        selectedTask = null;
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

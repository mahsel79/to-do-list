package com.grit.frontend.controller;

import com.grit.backend.controller.TaskController;
import com.grit.backend.model.Task;
import com.grit.frontend.util.LoggerUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;
import java.util.logging.Logger;

public class ToDoController {

    private static final Logger logger = LoggerUtil.getLogger(ToDoController.class.getName());

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
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;

    private final TaskController taskController;
    private Task selectedTask;

    // Constructor updated to accept TaskController as a dependency
    public ToDoController(TaskController taskController) {
        this.taskController = taskController;
    }

    @FXML
    public void initialize() {
        // Initialize table columns
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        completedColumn.setCellValueFactory(cellData -> cellData.getValue().completedProperty().asObject());

        // Load tasks from the backend
        loadTasksFromBackend();

        // Add selection listener for TableView
        taskTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedTask = newValue;
                descriptionField.setText(selectedTask.getDescription());
                checkBox.setSelected(selectedTask.isCompleted());
                saveButton.setVisible(false);
            }
        });

        saveButton.setVisible(false); // Hide the save button initially
    }

    private void loadTasksFromBackend() {
        logger.info("Loading tasks from backend...");
        List<Task> tasks = taskController.getAllTasks();
        taskTableView.setItems(FXCollections.observableArrayList(tasks));
        logger.info("Loaded " + tasks.size() + " tasks from backend.");
    }

    @FXML
    public void handleAddTask() {
        String description = descriptionField.getText();
        boolean isCompleted = checkBox.isSelected();

        if (description == null || description.trim().isEmpty()) {
            logger.warning("Attempted to add a task with an empty description.");
            showAlert(Alert.AlertType.ERROR, "Description cannot be empty.");
            return;
        }

        logger.info("Adding new task with description: " + description + " and completed status: " + isCompleted);
        taskController.createTask(description, isCompleted);
        loadTasksFromBackend(); // Reload tasks after adding a new one
        clearInputFields();
    }

    @FXML
    public void handleEditTask() {
        if (selectedTask == null) {
            logger.warning("Attempted to edit a task with no task selected.");
            showAlert(Alert.AlertType.WARNING, "Please select a task to edit.");
            return;
        }

        descriptionField.setText(selectedTask.getDescription());
        checkBox.setSelected(selectedTask.isCompleted());
        saveButton.setVisible(true);
        logger.info("Task selected for editing: ID=" + selectedTask.getId() + ", Description=" + selectedTask.getDescription());
    }

    @FXML
    public void handleSaveTask() {
        if (selectedTask == null) {
            logger.warning("Attempted to save a task with no task selected.");
            showAlert(Alert.AlertType.WARNING, "Please select a task to save.");
            return;
        }

        String newDescription = descriptionField.getText();
        boolean isCompleted = checkBox.isSelected();

        if (newDescription == null || newDescription.trim().isEmpty()) {
            logger.warning("Attempted to save a task with an empty description.");
            showAlert(Alert.AlertType.ERROR, "Description cannot be empty.");
            return;
        }

        logger.info("Saving updated task with ID: " + selectedTask.getId() + ", New Description: " + newDescription + ", New Completed status: " + isCompleted);
        taskController.updateTask(selectedTask.getId(), newDescription, isCompleted);
        loadTasksFromBackend();
        showAlert(Alert.AlertType.INFORMATION, "Task updated successfully!");

        clearInputFields();
        saveButton.setVisible(false);
    }

    @FXML
    public void handleDeleteTask() {
        if (selectedTask == null) {
            logger.warning("Attempted to delete a task with no task selected.");
            showAlert(Alert.AlertType.WARNING, "Please select a task to delete.");
            return;
        }

        logger.info("Deleting task with ID: " + selectedTask.getId());
        taskController.deleteTask(selectedTask.getId());
        loadTasksFromBackend();
        showAlert(Alert.AlertType.INFORMATION, "Task deleted successfully!");
        clearInputFields();
    }

    private void clearInputFields() {
        descriptionField.clear();
        checkBox.setSelected(false);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

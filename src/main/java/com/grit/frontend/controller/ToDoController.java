package com.grit.frontend.controller;

import com.grit.backend.controller.TaskController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.grit.model.Task;
import java.util.List;
import java.util.logging.Logger;
import com.grit.frontend.util.LoggerUtil;

public class ToDoController {

    private static final Logger logger = LoggerUtil.getLogger(ToDoController.class.getName()); // Logger

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

    private TaskController taskController;
    private Task selectedTask;

    public ToDoController() {
        // Initialize TaskController from backend
        taskController = new TaskController();
    }

    @FXML
    public void initialize() {
        // Initialize columns
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        completedColumn.setCellValueFactory(cellData -> cellData.getValue().completedProperty().asObject());

        // Load data from the backend
        loadTasksFromBackend();

        // Add demo data if the backend is empty
        if (taskController.getAllTasks().isEmpty()) {
            addDemoData();
        }

        // Add selection listener for TableView
        taskTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedTask = newValue;
                descriptionField.setText(selectedTask.getDescription());
                checkBox.setSelected(selectedTask.isCompleted());
                saveButton.setVisible(false);
            }
        });
    }

    private void loadTasksFromBackend() {
        logger.info("Loading tasks from backend...");
        List<Task> tasks = taskController.getAllTasks(); // Get tasks from backend
        taskTableView.setItems(FXCollections.observableArrayList(tasks)); // Load tasks into TableView
        logger.info("Loaded " + tasks.size() + " tasks from backend.");
    }

    private void addDemoData() {
        logger.info("Adding demo data as backend is empty.");
        // Adding demo tasks directly to the backend (mock behavior)
        taskController.createTask("Finish JavaFX tutorial", false);
        taskController.createTask("Buy groceries", false);
        taskController.createTask("Clean the house", true);
        loadTasksFromBackend(); // Reload tasks after adding demo data
        logger.info("Demo data added successfully.");
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

        // Add task via backend and reload tasks
        logger.info("Adding new task with description: " + description + " and completed status: " + isCompleted);
        taskController.createTask(description, isCompleted);
        loadTasksFromBackend();

        descriptionField.clear();
        checkBox.setSelected(false);
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

        // Update task in backend
        logger.info("Saving updated task with ID: " + selectedTask.getId() + ", New Description: " + newDescription + ", New Completed status: " + isCompleted);
        taskController.updateTask(selectedTask.getId(), newDescription, isCompleted);
        loadTasksFromBackend();

        showAlert(Alert.AlertType.INFORMATION, "Task updated successfully!");

        descriptionField.clear();
        checkBox.setSelected(false);
        saveButton.setVisible(false);
    }

    @FXML
    public void handleDeleteTask() {
        if (selectedTask == null) {
            logger.warning("Attempted to delete a task with no task selected.");
            showAlert(Alert.AlertType.WARNING, "Please select a task to delete.");
            return;
        }

        // Delete task via backend
        logger.info("Deleting task with ID: " + selectedTask.getId());
        taskController.deleteTask(selectedTask.getId());
        loadTasksFromBackend();

        showAlert(Alert.AlertType.INFORMATION, "Task deleted successfully!");
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

package com.grit.frontend.controller;

import com.grit.backend.controller.TaskController;
import com.grit.backend.repository.InMemoryTaskRepository;
import com.grit.backend.service.InMemoryTaskService;
import com.grit.backend.service.TaskService;
import com.grit.model.Task;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

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
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();

    public ToDoController() {
        TaskService taskService = new InMemoryTaskService(new InMemoryTaskRepository());
        this.taskController = new TaskController(taskService);
    }

    @FXML
    public void initialize() {
        LOGGER.info("Initializing ToDoController...");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        completedColumn.setCellValueFactory(cellData -> cellData.getValue().completedProperty().asObject());

        // Bind the TableView to the observable list
        taskTableView.setItems(tasks);

        // Listen for selection changes in TableView
        taskTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedTask = newValue;
            if (newValue != null) {
                LOGGER.info("Selected task: " + newValue.getDescription());
            }
        });

        // Load tasks initially
        loadTasksFromBackend();

        // Set up auto-refresh every 5 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            LOGGER.info("Auto-refreshing tasks...");
            loadTasksFromBackend();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
        timeline.play();
    }

    private void loadTasksFromBackend() {
        List<Task> taskList = taskController.getAllTasks();
        if (taskList != null && !taskList.isEmpty()) {
            tasks.setAll(taskList); // Update the observable list
            LOGGER.info("Loaded " + taskList.size() + " tasks.");
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
        taskController.createTask(description, isCompleted);
        loadTasksFromBackend(); // Refresh the task list
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
        loadTasksFromBackend(); // Refresh the task list
        clearForm();
    }

    @FXML
    public void handleDeleteTask() {
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "No task selected to delete.");
            return;
        }

        // Confirm with the user before deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected task?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                taskController.deleteTask(selectedTask.getId());
                loadTasksFromBackend(); // Refresh the task list
            }
        });
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
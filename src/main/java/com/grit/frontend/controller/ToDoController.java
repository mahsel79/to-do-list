package com.grit.frontend.controller;

import com.grit.frontend.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ToDoController {

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
    private CheckBox checkBox; // This must match the fx:id in the FXML
    @FXML
    private Button saveButton;

    private ObservableList<Task> taskList;
    private Task selectedTask;

    public ToDoController() {
        taskList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        // Initialize columns
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        completedColumn.setCellValueFactory(cellData -> cellData.getValue().completedProperty());

        // Add demo data
        taskList.add(new Task(1, "Finish JavaFX tutorial", true));
        taskList.add(new Task(2, "Buy groceries", false));
        taskList.add(new Task(3, "Clean the house", true));

        // Set demo data to the table view
        taskTableView.setItems(taskList);

        // Listen for selection in the table
        taskTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedTask = newValue;
                // Clear the details when a new task is selected
                descriptionField.clear();
                checkBox.setSelected(false);
                saveButton.setVisible(false); // Hide the Save button until Edit is clicked
            }
        });
    }

    @FXML
    public void handleAddTask() {
        String description = descriptionField.getText();
        boolean isCompleted = checkBox.isSelected();

        // Check if description is not empty
        if (description == null || description.trim().isEmpty()) {
            showAlert(AlertType.ERROR, "Description cannot be empty.");
            return;
        }

        Task newTask = new Task(taskList.size() + 1, description, isCompleted);
        taskList.add(newTask);
        taskTableView.setItems(taskList);
        descriptionField.clear();
        checkBox.setSelected(false); // Reset checkbox
    }

    @FXML
    public void handleEditTask() {
        if (selectedTask == null) {
            showAlert(AlertType.WARNING, "Please select a task to edit.");
            return;
        }

        // Autofill the selected task details
        descriptionField.setText(selectedTask.getDescription());
        checkBox.setSelected(selectedTask.isCompleted());

        // Show the Save button to allow editing
        saveButton.setVisible(true);
    }

    @FXML
    public void handleDeleteTask() {
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskList.remove(selectedTask);
        }
    }

    @FXML
    public void handleSaveTask() {
        if (selectedTask == null) {
            showAlert(AlertType.WARNING, "Please select a task to save.");
            return;
        }

        // Update task description and completion status
        String newDescription = descriptionField.getText();
        boolean isCompleted = checkBox.isSelected();

        selectedTask.descriptionProperty().set(newDescription);
        selectedTask.completedProperty().set(isCompleted);

        taskTableView.refresh(); // Refresh the table view to show updated data

        showAlert(AlertType.INFORMATION, "Task updated successfully!");

        descriptionField.clear();
        checkBox.setSelected(false);  // Reset checkbox
        saveButton.setVisible(false);  // Hide save button after editing
    }

    // Show alert with the given message
    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

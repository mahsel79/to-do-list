### **To-Do List Management System**

This project is a Java-based To-Do List Management System, designed to manage tasks interactively through a simple user interface. Users can create, view, update, and delete tasks, as well as track their completion status.

---

### **Table of Contents**
1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Technologies and Libraries](#technologies-and-libraries)
4. [Code Structure and Modules](#code-structure-and-modules)
5. [Task Management Process](#task-management-process)
6. [How to Run the Project](#how-to-run-the-project)

---

### **Project Overview**
The goal of this project is to create a lightweight task management system. Users can:
- Create new tasks.
- View all existing tasks.
- Update task descriptions or their completion status.
- Delete tasks and update task IDs to maintain sequential order.
- Track task status (completed or not completed).

The application integrates a JavaFX-based front-end for user interaction and a backend to manage tasks in memory.

---

### **Features**
1. **Create Tasks**: Add new tasks with descriptions and completion status.
2. **View Tasks**: List all tasks with their IDs, descriptions, and statuses.
3. **Update Tasks**: Modify task descriptions and toggle their completion status.
4. **Delete Tasks**: Remove tasks by their unique ID.
5. **Task ID Management**: Automatically update IDs to ensure sequential order after deletions.
6. **Interactive Menu**: A user-friendly console or JavaFX interface for managing tasks.

---

### **Technologies and Libraries**

#### **Core Technologies**
- **Java 21**: Built using modern Java features for efficiency and maintainability.
- **JavaFX**: Provides a graphical user interface for task management.
- **Java Collections Framework**: Used to manage tasks in memory with lists and maps.

#### **Key Libraries**
- **java.util**: For utilities like lists, maps, and optional data structures.
- **java.util.logging**: For implementing logging functionality.
- **javafx.base/javafx.scene**: For JavaFX-based UI components.

---

### **Code Structure and Modules**

The project is organized for clarity and scalability:

```
to-do-list/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── grit/
│   │   │   │   │   ├── backend/         // Backend logic and data
│   │   │   │   │   │   ├── model/       // Task entity
│   │   │   │   │   │   │   ├── Task.java
│   │   │   │   │   │   ├── service/     // Task services
│   │   │   │   │   │   │   ├── TaskService.java
│   │   │   │   │   │   │   ├── InMemoryTaskService.java
│   │   │   │   │   ├── frontend/        // Frontend UI and utilities
│   │   │   │   │   │   ├── util/        // Logging and helpers
│   │   │   │   │   │   │   ├── LoggerUtil.java
│   │   │   │   │   │   ├── ui/          // JavaFX UI components
│   │   │   │   │   │   │   ├── TaskController.java
│   │   │   │   │   │   │   ├── MainApp.java
│   ├── resources/
│   │   ├── com/grit/frontend/           // FXML files for JavaFX
├── README.md                            // Project description and instructions
```

---

### **Task Management Process**

#### **1. Create Tasks**
- Users provide a task description and completion status.
- A new task is created and stored in memory with a unique ID.

#### **2. View Tasks**
- Users can view all tasks in a list.
- Each task includes its ID, description, and status.

#### **3. Update Tasks**
- Users provide the task ID and updated details.
- The task's description and/or completion status are modified.

#### **4. Delete Tasks**
- Users specify the ID of the task to delete.
- The task is removed, and IDs are updated to maintain sequential order.

#### **5. Maintain Task Order**
- After a deletion, the task list is sorted, and IDs are reassigned sequentially.

---

### **How to Run the Project**

#### **Prerequisites**
1. **Java 21 or Later**: Ensure Java is installed and properly configured.
2. **IDE**: (Optional) Use IntelliJ IDEA for development and execution.
3. **JavaFX Runtime**: Required for the GUI interface.

#### **Steps to Run**
1. Clone or download the project:
   ```bash
   git clone https://github.com/username/to-do-list.git
   ```
2. Navigate to the project directory:
   ```bash
   cd to-do-list
   ```
3. Compile the project:
   ```bash
   javac -d out src/main/java/com/grit/**/*.java
   ```
4. Run the application:
   ```bash
   java -cp out com.grit.frontend.ui.MainApp
   ```
5. The GUI or console interface will appear for task management.


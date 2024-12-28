### **To-Do List Management System**

This project is a Java-based To-Do List Management System that provides a graphical user interface (GUI) for managing tasks and supports a lightweight HTTP server for JSON-based CRUD operations. Users can create, view, update, and delete tasks seamlessly via the GUI or interact with the backend APIs for external integrations.

---

### **Table of Contents**
1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Technologies and Libraries](#technologies-and-libraries)
4. [Code Structure and Modules](#code-structure-and-modules)
5. [Task Management Process](#task-management-process)
6. [API Endpoints](#api-endpoints)
7. [How to Run the Project](#how-to-run-the-project)

---

### **Project Overview**
The goal of this project is to create a lightweight task management system that can be operated via a GUI or RESTful APIs. Users can:
- Manage tasks directly in the GUI.
- Interact with the backend through HTTP API calls.
- Perform CRUD (Create, Read, Update, Delete) operations on tasks using JSON-based endpoints.

The project uses an in-memory data store backed by a `tasks.json` file for persistence, ensuring fast and efficient operations without requiring a database setup. Real-time synchronization between the GUI and the backend ensures consistency.

---

### **Features**
1. **GUI-based Task Management**:
   - Create, view, update, and delete tasks with a JavaFX interface.
   - Auto-refreshes the UI after modifications.
2. **JSON API for Task Operations**:
   - Perform CRUD operations through RESTful HTTP endpoints.
3. **Task ID Management**:
   - Automatically update task IDs to maintain sequential order after deletions.
4. **Real-Time Sync**:
   - GUI interacts with the backend through API calls to ensure consistency.
5. **Lightweight HTTP Server**:
   - Built-in HTTP server for API requests, eliminating the need for external web servers.
6. **Persistence**:
   - Tasks are stored in a `tasks.json` file for persistence across application restarts.
7. **Logging**:
   - Detailed logs for operations, errors, and server activity.

---

### **Technologies and Libraries**

#### **Core Technologies**
- **Java 21**: Modern Java features for performance and simplicity.
- **JavaFX**: Provides a graphical interface for task management.
- **HTTP Server**: Custom HTTP server implementation for handling API requests.

#### **Key Libraries**
- **java.util**: Utilities like collections, optional, and logging.
- **java.util.logging**: Implements logging for debugging and operational tracking.
- **java.net.http**: Handles HTTP request processing.
- **com.fasterxml.jackson**: For JSON serialization and deserialization.

---

### **Code Structure and Modules**

The project is structured for clarity and scalability:

```
to-do-list/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ ├── com/
│ │ │ │ ├── grit/
│ │ │ │ │ ├── backend/ // Backend logic and HTTP server
│ │ │ │ │ │ ├── controller/ // Task API endpoints
│ │ │ │ │ │ │ ├── TaskController.java
│ │ │ │ │ │ ├── model/ // Task entity
│ │ │ │ │ │ │ ├── Task.java
│ │ │ │ │ │ ├── service/ // Task services
│ │ │ │ │ │ │ ├── TaskService.java
│ │ │ │ │ │ │ ├── InMemoryTaskService.java
│ │ │ │ │ │ ├── server/ // HTTP server setup
│ │ │ │ │ │ │ ├── HttpServer.java
│ │ │ │ ├── frontend/ // Frontend GUI and logic
│ │ │ │ │ ├── controller/ // GUI controllers
│ │ │ │ │ │ ├── ToDoController.java
│ │ │ │ │ ├── model/ // GUI task entity
│ │ │ │ │ │ ├── Task.java
│ │ │ │ │ ├── MainApp.java // Main application entry point
│ │ │ │ ├── util/ // Logger utilities
│ │ │ │ │ ├── LoggerUtil.java
├── tasks.json // File for task persistence
├── README.md // Project description and instructions
```

---

### **Task Management Process**

#### **1. GUI-Based Management**
- **Create Tasks**: Add tasks by entering a description and selecting a completion status.
- **View Tasks**: List all tasks, including their ID, description, and completion status.
- **Update Tasks**: Modify task details via a user-friendly interface.
- **Delete Tasks**: Remove tasks and update task IDs automatically.

#### **2. API-Based Management**
- External systems can interact with the backend via HTTP APIs.
- All API responses are in JSON format for compatibility and ease of integration.

---

### **API Endpoints**

1. **Retrieve All Tasks**
    - **GET /tasks**
    - **Response**: JSON array of tasks.
    - **Example**:
      ```json
      [
        { "id": 1, "description": "Buy groceries", "completed": false },
        { "id": 2, "description": "Finish homework", "completed": true }
      ]
      ```

2. **Create a Task**
    - **POST /tasks**
    - **Request Body**:
      ```json
      { "description": "Walk the dog", "completed": false }
      ```
    - **Response**: Created task with unique ID.

3. **Update a Task**
    - **PUT /tasks/{id}**
    - **Request Body**:
      ```json
      { "description": "Walk the dog", "completed": true }
      ```
    - **Response**: Updated task.

4. **Delete a Task**
    - **DELETE /tasks/{id}**
    - **Response**:
        - **200 OK**: Task deleted successfully.
        - **404 Not Found**: Task with given ID not found.

---

### **How to Run the Project**

#### **Prerequisites**
1. **Java 21 or Later**: Ensure Java is installed and properly configured.
2. **JavaFX Runtime**: Required for GUI functionality.
3. **IDE**: IntelliJ IDEA or another IDE for development and testing.

#### **Steps to Run**
1. Clone or download the project:
   ```bash
   git clone https://github.com/mahsel79/to-do-list.git
   ```
2. Navigate to the project directory:
   ```bash
   cd to-do-list
   ```
3. Compile the project:
   ```bash
   javac -d out src/main/java/com/grit/**/*.java
   ```
4. Start the HTTP server:
   ```bash
   java -cp out com.grit.backend.server.HttpServer
   ```
5. Run the GUI application:
   ```bash
   java -cp out com.grit.frontend.ui.MainApp
   ```

---

### **Usage**
- Open the GUI for an interactive experience.
- Use RESTful APIs for programmatic task management via tools like Postman or `curl`.

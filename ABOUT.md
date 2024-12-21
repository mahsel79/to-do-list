# **To-Do List Application**

## **Introduction**

This repository contains a simple To-Do List application that allows users to manage tasks with features like adding, editing, and deleting. The project is built with a modular structure, divided into backend and frontend components:

- **Backend:** Handles API requests and manages task data in memory using standard Java.
- **Frontend:** A JavaFX-based graphical user interface enabling users to interact with the system.

The application follows object-oriented principles, utilizing interfaces, abstract classes, and polymorphism for a clean, extendable design.

---

## **Project Structure**

### **Backend**
- **Controller:** Handles API endpoints.
- **Service:** Contains business logic for managing tasks.
- **Repository:** Defines an interface for data access operations.
- **Model:** Represents task entities (`Task` class).

### **Frontend**
- Built with JavaFX, the frontend provides:
    - A list view of all tasks.
    - Buttons and forms to add, edit, or delete tasks.

### **Utilities**
- Logging functionality is implemented to assist with debugging and tracking operations.

---

## **Features**

### **Backend (Server)**
- **Implemented API Endpoints:**
    - `GET /tasks` - Retrieve all tasks.
    - `POST /tasks` - Add a new task.
    - `PUT /tasks/{id}` - Update an existing task.
    - `DELETE /tasks/{id}` - Remove a task by ID.
- **Data Management:** Tasks are stored in memory using a `HashMap`, ensuring data persists during the server runtime.

### **Frontend (JavaFX)**
- Displays a list of tasks with details.
- Enables task management with:
    - **Add Task:** Enter a title and description to create a task.
    - **Edit Task:** Modify the details of an existing task.
    - **Delete Task:** Remove a task from the list.
- Automatically refreshes the UI after any action.

---

## **Design Choices**

### **Object-Oriented Principles**
- **Interfaces:** The `TaskRepository` interface defines CRUD operations, ensuring modularity and flexibility.
- **Abstract Classes:** `TaskService` provides a base structure for task management logic, extended by `InMemoryTaskService` for in-memory storage.
- **Polymorphism:** Used in the repository and service layers for extensibility.

### **Data Structure**
- A `HashMap` is used for efficient storage and retrieval of tasks, keyed by task ID. This avoids the need for a database while keeping operations fast.

---

## **Future Improvements**
While the current implementation meets all requirements, potential enhancements include:
- **Persistent Storage:** Transitioning from in-memory storage to a database for long-term data retention.
- **Testing:** Adding unit tests for backend methods and API endpoints.
- **Enhanced UI:** Improving the JavaFX interface with features like task filtering and sorting.
- **Error Handling:** Providing detailed API error responses and better user feedback.

---

## **How to Run**

1. Clone the repository:
   ```bash
   git clone https://github.com/mahsel79/to-do-list.git
   cd to-do-list
   ```
2. Start the backend server:
   ```bash
   javac -d bin src/com/grit/backend/*.java
   java -cp bin com.grit.backend.MainServer
   ```
3. Launch the frontend:
   ```bash
   javac -d bin src/com/grit/frontend/*.java
   java -cp bin com.grit.frontend.MainApp
   ```

---

## **Technical Insights**

### **Why Use In-Memory Storage?**
- Simplicity: Avoids the complexity of setting up a database.
- Efficiency: Tasks are stored in memory during runtime, ensuring fast access.

### **Design Decisions**
- **Interfaces and Abstract Classes:** Ensure modularity and easy extension for future changes.
- **Error Handling:** Minimal implementation; future work will include improved error reporting and validations.

---

## **Project Goals**

This project demonstrates:
- Integration of a custom backend with a JavaFX frontend.
- Implementation of CRUD operations using object-oriented design.
- Usage of in-memory storage for task data.

It serves as a foundational project for learning modular development and can be extended in multiple ways, such as adding persistent storage or enhancing the frontend UI.


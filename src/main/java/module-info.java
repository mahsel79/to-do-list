module com.grit.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.logging;
    requires jdk.httpserver;
    requires com.fasterxml.jackson.databind; // Added Jackson dependency

    opens com.grit.frontend to javafx.fxml;
    exports com.grit.frontend;
    exports com.grit.frontend.controller;
    opens com.grit.frontend.controller to javafx.fxml;

    // Export the model package to Jackson
    exports com.grit.model; // Add this line to allow Jackson to use it
}

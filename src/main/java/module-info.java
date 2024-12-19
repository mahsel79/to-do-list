module com.grit.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.logging;
    requires jdk.httpserver;

    opens com.grit.frontend to javafx.fxml;
    exports com.grit.frontend;
    exports com.grit.frontend.controller;
    opens com.grit.frontend.controller to javafx.fxml;
}
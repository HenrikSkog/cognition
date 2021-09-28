module cognition.ui {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    requires cognition.core;
    opens ui to javafx.graphics, javafx.fxml;
    opens ui.controllers to javafx.fxml, javafx.graphics;
}
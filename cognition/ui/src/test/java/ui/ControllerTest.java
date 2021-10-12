package ui;

import javafx.fxml.FXMLLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import ui.controllers.Controller;
import ui.controllers.LoginController;

public class ControllerTest extends ApplicationTest {
    private Controller controller;

    @BeforeEach
    void setUp() {
        // Because Controller is an abstract class, we use an extending controller for testing.
        controller = new LoginController();
    }

    @Test
    @DisplayName("Can get loader based on FXML.")
    void canGetLoaderBasedOnFxml() {
        FXMLLoader loader = controller.getLoader("LoginTest");
        Assertions.assertNotNull(loader);
    }
}

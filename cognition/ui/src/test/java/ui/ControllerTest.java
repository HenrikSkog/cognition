package ui;

import javafx.fxml.FXMLLoader;
import json.CognitionStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import ui.controllers.Controller;
import ui.controllers.LoginController;

import java.io.IOException;

public class ControllerTest extends ApplicationTest {
  private Controller controller;

  @BeforeEach
  void setUp() throws IOException {
    // Because Controller is an abstract class, we use an extending controller for
    // testing.
    controller = new LoginController(new CognitionStorage());
  }

  @Test
  @DisplayName("Can get loader based on FXML.")
  void canGetLoaderBasedOnFxml() {
    FXMLLoader loader = controller.getLoader("LoginTest");
    Assertions.assertNotNull(loader);
  }
}

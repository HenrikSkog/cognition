package integration_tests;

import api.RestApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import ui.LoginController;
import ui.RemoteCognitionAccess;
import java.io.IOException;
import java.net.URL;

public class AppTest extends ApplicationTest {
  private LoginController loginController;
  private Scene scene;

  @Override
  public void start(final Stage stage) throws Exception {
    // Load FXML view
    FXMLLoader loader = loadFromUserInterface("Login");

    // Set state in controller
    this.loginController = new LoginController(
            new RemoteCognitionAccess(RestApplication.TEST_PORT)
    );
    loader.setController(loginController);

    // Switch stage
    scene = new Scene(loader.load());
    stage.setScene(scene);
    stage.show();
  }

  @Test
  @DisplayName("Controller is defined.")
  void controllerIsDefined() {
    Assertions.assertNotNull(loginController);
  }

  /**
   * Loads an FXML view from the ui module.
   * This is used in order to not duplicate the views across Maven modules.
   *
   * @param fxml is the name of the FXML view.
   * @return an FXMLLoader for the given FXML view.
   * @throws IOException if an error occurs when loading the FXML view.
   */
  private FXMLLoader loadFromUserInterface(String fxml) throws IOException {
    FXMLLoader loader = new FXMLLoader();

    // Find the FXML file relative to the App class in the ui module
    URL url = ui.App.class.getResource("views/" + fxml + ".fxml");

    loader.setLocation(url);

    return loader;
  }
}

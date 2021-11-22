package integration_tests;

import api.RestApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;
import ui.LoginController;
import ui.RemoteCognitionAccess;

public class AppTest extends ApplicationTest {
  private LoginController loginController;
  private Scene scene;

  private final String validUsername = "valid-username";
  private final String validPassword = "valid-password";

  @BeforeAll
  static void beforeAll() {
    // Ensure that we're using file for testing in persistent storage
    RestApplication.setTestMode(true);

    // Start web server
    RestApplication.start();
  }

  @AfterAll
  static void afterAll() {
    RestApplication.stop();
  }

  @AfterEach
  void tearDown() {
    IntegrationTestsHelper.clearTestStorage();
  }

  @Override
  public void start(final Stage stage) throws Exception {
    // Load FXML view
    FXMLLoader loader = IntegrationTestsHelper.loadFromUserInterface("Login");

    // Set state in controller.
    // The controller uses a RemoteCognitionAccess configured with the port for testing
    this.loginController = new LoginController(new RemoteCognitionAccess(RestApplication.TEST_PORT));
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

  // @Test
  // @DisplayName("Client can connect to web server.")
  // void clientCanConnectToWebServer() {
  //   User user = new User(validUsername, validPassword);
  //   this.loginController.getRemoteCognitionAccess().create(user);
  // }
}

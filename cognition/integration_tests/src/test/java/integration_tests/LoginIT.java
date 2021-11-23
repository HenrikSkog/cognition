package integration_tests;

import api.RestApplication;
import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;
import ui.LoginController;
import ui.RemoteCognitionAccess;
import java.io.IOException;

public class LoginIT extends ApplicationTest {
  private LoginController loginController;
  private Scene scene;
  private final IntegrationTestsHelper helper = new IntegrationTestsHelper();

  @BeforeAll
  static void beforeAll() {
    // Ensure that we're using file for testing in persistent storage, and start web server
    RestApplication.setTestMode(true);
    RestApplication.main(new String[]{});
  }

  @AfterAll
  static void afterAll() {
    RestApplication.stop();
  }

  @AfterEach
  void tearDown() {
    helper.clearTestStorage();
  }

  @Override
  public void start(final Stage stage) throws Exception {
    // Load FXML view
    FXMLLoader loader = helper.loadFromUserInterface("Login");

    // Set state in controller.
    // The controller uses a RemoteCognitionAccess configured with the port for testing
    this.loginController = new LoginController(new RemoteCognitionAccess());
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

  @Test
  @DisplayName("Client can connect to web server.")
  void clientCanConnectToWebServer() throws IOException, InterruptedException {
    User user = new User(helper.getUsername(), helper.getPassword());

    // Create sample user
    this.loginController.getRemoteCognitionAccess().create(user);

    // Read stored user
    User parsedUser = loginController.getRemoteCognitionAccess().read(helper.getUsername());

    // Assertions
    Assertions.assertNotNull(parsedUser);
    Assertions.assertEquals(helper.getUsername(), parsedUser.getUsername());
  }
}

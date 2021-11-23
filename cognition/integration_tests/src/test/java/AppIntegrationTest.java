import api.RestApplication;
import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import json.CognitionStorage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;
import ui.LoginController;
import ui.RemoteCognitionAccess;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.fail;

public class AppIntegrationTest extends ApplicationTest {
  private LoginController loginController;
  private Scene scene;

  @BeforeAll
  static void beforeAll() {
    // Ensure that we're using file for testing in persistent storage, and start web server
    RestApplication.main(new String[]{"testmode"});
  }

  @AfterEach
  void tearDown() {
    clearTestStorage();
  }

  @Override
  public void start(final Stage stage) throws Exception {
    // Load FXML view
    FXMLLoader loader = loadFromUserInterface("Login");

    // Set state in controller.
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

  @Test
  @DisplayName("Client can connect to web server.")
  void clientCanConnectToWebServer() throws IOException, InterruptedException {
    String username = "it-username";
    String password = "it-password";

    User user = new User(username, password);

    // Create sample user
    this.loginController.getRemoteCognitionAccess().create(user);

    // Read stored user
    User parsedUser = loginController.getRemoteCognitionAccess().read(username);

    // Assertions
    Assertions.assertNotNull(parsedUser);
    Assertions.assertEquals(username, parsedUser.getUsername());
  }

  /**
   * Empties the JSON data in file at the storage path. Used before validating the
   * return type when user storage is empty.
   */
  private void clearTestStorage() {
    try (FileWriter writer = new FileWriter(String.valueOf(new CognitionStorage("cognitionTest.json").getStoragePath()))) {
      writer.write("");
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Loads an FXML view from the ui module.
   * This is used in order to not duplicate the views across Maven modules.
   *
   * @param fxml is the name of the FXML view.
   * @return an FXMLLoader for the given FXML view.
   */
  private FXMLLoader loadFromUserInterface(String fxml) {
    FXMLLoader loader = new FXMLLoader();

    // Find the FXML file relative to the App class in the ui module
    URL url = ui.App.class.getResource("views/" + fxml + ".fxml");

    loader.setLocation(url);

    return loader;
  }
}

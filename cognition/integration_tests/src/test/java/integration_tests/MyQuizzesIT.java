package integration_tests;

import api.RestApplication;
import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;
import ui.MyQuizzesController;
import ui.RemoteCognitionAccess;
import java.io.IOException;

public class MyQuizzesIT extends ApplicationTest {

  private MyQuizzesController myQuizzesController;
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
    FXMLLoader loader = helper.loadFromUserInterface("MyQuizzes");

    User loggedInUser = new User(helper.getUsername(), helper.getPassword());

    // Set state in controller.
    // The controller uses a RemoteCognitionAccess configured with the port for testing
    this.myQuizzesController = new MyQuizzesController(loggedInUser, new RemoteCognitionAccess());
    loader.setController(myQuizzesController);

    // Switch stage
    scene = new Scene(loader.load());
    stage.setScene(scene);
    stage.show();
  }

  @Test
  @DisplayName("Controller is defined.")
  void controllerIsDefined() {
    Assertions.assertNotNull(myQuizzesController);
  }

  @Test
  @DisplayName("Client can connect to web server.")
  void clientCanConnectToWebServer() throws IOException, InterruptedException {
    // Read stored user
    User parsedUser = myQuizzesController.getRemoteCognitionAccess().read(helper.getUsername());

    // Assertions
    Assertions.assertNotNull(parsedUser);
    Assertions.assertEquals(helper.getUsername(), parsedUser.getUsername());
  }
}

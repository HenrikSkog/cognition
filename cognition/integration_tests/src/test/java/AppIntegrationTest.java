import api.RestApplication;
import core.Quiz;
import core.User;
import core.tools.Tools;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests and verifies that the client application can successfully communicate with a
 * running Spring Boot web server, the {@link RestApplication} class.
 */
public class AppIntegrationTest extends ApplicationTest {
  private LoginController loginController;
  private Scene scene;

  private String validUsername = "it-username";
  private String validPassword = "it-password";

  private List<User> users;

  @BeforeAll
  static void beforeAll() {
    // Ensure that we're using file for testing in persistent storage, and start web server
    RestApplication.main(new String[]{"testmode"});

    // RestApplication gets implicitly shut down when all test are done running.
  }

  @BeforeEach
  void setUp() {
    this.users = generateTestData();
    // Write test data to local storage
    writeTestData(users);
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
    // Read stored user
    User parsedUser = loginController.getRemoteCognitionAccess().read(users.get(0).getUsername());

    // Assertions
    Assertions.assertNotNull(parsedUser);
    Assertions.assertEquals(users.get(0), parsedUser);
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

  @Test
  @DisplayName("Client can read all users")
  void clientCanReadAllUsers() {
    try {
      List<User> readUsers = loginController.getRemoteCognitionAccess().readUsers();

      for (int i = 0; i < users.size(); i++) {
        Assertions.assertEquals(users.get(i), readUsers.get(i));
      }
    } catch (InterruptedException | IOException e) {
      fail();
    }
  }

  @Test
  @DisplayName("Client can update a user")
  void clientCanUpdateUser() {
    try {
      User user = users.get(0);
      user.setPassword("new-password");

      loginController.getRemoteCognitionAccess().update(user);

      User updatedUser = loginController.getRemoteCognitionAccess().read(user.getUsername());

      Assertions.assertEquals(user, updatedUser);
    } catch( InterruptedException | IOException e) {
      fail();
    }
  }

  @Test
  @DisplayName("Client can delete a user")
  void clientCanDelete() {
    User user = users.get(0);
    users.remove(0);

    try {
      loginController.getRemoteCognitionAccess().delete(user.getUsername());
      List<User> readUsers = loginController.getRemoteCognitionAccess().readUsers();

      for (int i = 0; i < users.size(); i++) {
        Assertions.assertEquals(users.get(i), readUsers.get(i));
      }

    } catch( InterruptedException | IOException e) {
      fail();
    }
  }

  @Test
  @DisplayName("Client can read a quiz by id")
  void clientCanReadQuiz() {
    // Try to read 2nd quiz in 2nd user
    try {
      String quizId = users.get(1).getQuizzes().get(1).getUuid();
      String readQuizId = loginController
              .getRemoteCognitionAccess()
              .getQuizByUuid(users.get(1).getQuizzes().get(1).getUuid())
                  .getUuid();


      Assertions.assertEquals(quizId, readQuizId);
    } catch (IOException | InterruptedException e) {
      fail();
    }
  }

  @Test
  @DisplayName("Client can read all quizzes")
  void clientCanReadQuizzes() {
    //
  }

  private List<User> generateTestData() {
    List<User> users = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      User user = new User(validUsername + i, validPassword + i);

      for (int j = 0; j < 3; j++) {
        user.addQuiz(new Quiz(Tools.createUuid(), "front" + j, "back" + j));
      }

      users.add(user);
    }
    return users;
  }

  private void writeTestData(List<User> users) {
    users.forEach(user ->
    {
      try {
        this.loginController.getRemoteCognitionAccess().create(user);
      } catch (InterruptedException | IOException e) {
        fail();
      }
    });
  }
}

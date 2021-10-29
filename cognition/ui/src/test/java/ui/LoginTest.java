package ui;

import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import json.CognitionStorage;
import org.junit.jupiter.api.*;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import ui.controllers.LoginController;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import static core.tools.Tools.createUuid;
import static org.junit.jupiter.api.Assertions.fail;

public class LoginTest extends ApplicationTest {
  private final String validUsername = "valid-user";
  private final String validPassword = "valid-password";
  private Scene scene;
  private LoginController loginController;
  private CognitionStorage cognitionStorage;

  @BeforeEach
  void setUp() {
    try {
      // Add a user, such that storage is not empty. Empty storage is tested in core
      // module.
      cognitionStorage.create(new User(createUuid(), "placeholder", "placeholder"));
    } catch (IOException e) {
      fail();
    }
  }

  @AfterEach
  void tearDown() {
    clearUserStorage();
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = getLoader("Login");

    this.cognitionStorage = new CognitionStorage("cognitionTest.json");
    // in the app there is no logical way for Create Quiz to be accessed without a logged in user. Thus, we create a fake user here to emulate it
    this.loginController = new LoginController(cognitionStorage);

    loader.setController(loginController);

    scene = new Scene(loader.load());
    stage.setScene(scene);
    stage.show();
  }

  private FXMLLoader getLoader(String fxml) {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("views/" + fxml + ".fxml"));
    return loader;
  }

  @Test
  @DisplayName("Controller is defined.")
  void controllerIsDefined() {
    Assertions.assertNotNull(loginController);
  }

  @Test
  @DisplayName("Storage is defined.")
  void storageIsDefined() {
    Assertions.assertNotNull(cognitionStorage);
  }

  /**
   * Tests if an existing user in local storage can log in. Note that this test is
   * currently, as it is not completely done.
   */
  @Test
  @DisplayName("Existing user can log in.")
  void existingUserCanLogIn() {
    User user = new User(createUuid(), validUsername, validPassword);

    // Create sample user
    try {
      cognitionStorage.create(user);
    } catch (IOException e) {
      fail();
    }

    clickOn("#usernameInput").write(validUsername);
    clickOn("#passwordInput").write(validPassword);
    clickOn("#loginButton");

    // Check that we loaded Dashboard view
    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Dashboard"));
  }

  @Test
  @DisplayName("Can go to register view.")
  void canGoToRegisterView() {
    // Check that the Login view was loaded correctly
    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Login"));

    // Click to switch view
    clickOn("#goToRegisterButton");

    // Check that the Register view has loaded correctly
    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Register"));
  }

  @Test
  @DisplayName("Non-existing user cannot log in.")
  void nonExistingUserCannotLogIn() {
    // This username and password is not in local storage, as users.json is cleared
    // after each test
    verifyInputData("non-existing-user", "non-existing-password", loginController.getFeedbackErrorMessage());
  }

  /**
   * A helper method for verifying input data.
   *
   * @param username is the provided username in the input form
   * @param password is the provided password in the input form
   * @param feedback is the provided feedback from the form
   */
  private void verifyInputData(String username, String password, String feedback) {
    // Input data into UI
    clickOn("#usernameInput").write(username);
    clickOn("#passwordInput").write(password);
    clickOn("#loginButton");

    // Validate that user got correct feedback in UI
    FxAssert.verifyThat("#feedback", LabeledMatchers.hasText(feedback));
  }

  /**
   * Empties the JSON data in file at the storage path. Used before validating the
   * return type when user storage is empty.
   */
  private void clearUserStorage() {
    try (FileWriter writer = new FileWriter(cognitionStorage.getStoragePath())) {
      writer.write("");
    } catch (IOException e) {
      fail();
    }
  }
}

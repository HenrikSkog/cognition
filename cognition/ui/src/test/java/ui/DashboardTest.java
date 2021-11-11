package ui;

import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import json.CognitionStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;
import static ui.TestFxHelper.waitForFxEvents;

/**
 * DashboardTest tests that all functionality works as intended
 * for the Dashboard view.
 */
public class DashboardTest extends ApplicationTest {
  private final String validUsername = "valid-username";
  private final String validPassword = "valid-password";
  private Scene scene;
  private DashboardController dashboardController;
  private RemoteCognitionAccess remoteCognitionAccess;

  @AfterEach
  void tearDown() {
    clearUserStorage();
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = getLoader("Dashboard");

    this.remoteCognitionAccess = new RemoteCognitionAccess(AppTest.TEST_PORT);

    // in the app there is no logical way for Create Quiz to be accessed without a
    // logged-in user. Thus, we create a fake user here to emulate it
    User loggedInUser = new User(validUsername, validPassword);

    remoteCognitionAccess.create(loggedInUser);
    // in the app there is no logical way for Create Quiz to be accessed without a
    // logged-in user. Thus, we create a fake user here to emulate it
    this.dashboardController = new DashboardController(loggedInUser, remoteCognitionAccess);

    loader.setController(dashboardController);

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
    Assertions.assertNotNull(dashboardController);
  }

  @Test
  @DisplayName("Storage is defined.")
  void storageIsDefined() {
    Assertions.assertNotNull(remoteCognitionAccess);
  }

  @Test
  @DisplayName("User can log out")
  void userCanLogOut() {
    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Dashboard"));
    waitForFxEvents();

    clickOn("#signOutButton");
    waitForFxEvents();

    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Login"));
    waitForFxEvents();
  }

  @Test
  @DisplayName("Can switch to Create Quiz.")
  void canSwitchToCreateQuiz() {
    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Dashboard"));
    waitForFxEvents();

    clickOn("#createQuizButton");
    waitForFxEvents();

    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Quiz"));
    waitForFxEvents();
  }

  @Test
  @DisplayName("Can switch to My Quizzes.")
  void canSwitchToMyQuizzes() {
    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Dashboard"));
    waitForFxEvents();

    clickOn("#switchToMyQuizzesButton");
    waitForFxEvents();

    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("MyQuizzes"));
    waitForFxEvents();
  }

  /**
   * Empties the JSON data in file at the storage path. Used before validating the
   * return type when user storage is empty.
   */
  private void clearUserStorage() {
    try (FileWriter writer = new FileWriter(String.valueOf(new CognitionStorage("cognitionTest.json").getStoragePath()))) {
      writer.write("");
    } catch (IOException e) {
      fail();
    }
  }
}

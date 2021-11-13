package ui;

import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import json.CognitionStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextMatchers;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;
import static ui.TestFxHelper.waitForFxEvents;

public class QuizTest extends ApplicationTest {
  private final String validUsername = "valid-username";
  private final String validPassword = "valid-password";
  private Scene scene;
  private QuizController quizController;
  private RemoteCognitionAccess remoteCognitionAccess;


  @AfterEach
  void tearDown() {
    clearUserStorage();
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = getLoader("Quiz");

    RemoteCognitionAccess remoteCognitionAccess = new RemoteCognitionAccess(AppTest.TEST_PORT);
    this.remoteCognitionAccess = remoteCognitionAccess;

    // In the app, there is no logical way for Create Quiz to be accessed without a
    // logged-in user. Thus, we create a fake user here to emulate it
    User loggedInUser = new User(validUsername, validPassword);

    remoteCognitionAccess.create(loggedInUser);

    quizController = new QuizController(loggedInUser, remoteCognitionAccess);
    loader.setController(quizController);

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
    Assertions.assertNotNull(quizController);
  }

  @Test
  @DisplayName("Storage is defined.")
  void storageIsDefined() {
    Assertions.assertNotNull(remoteCognitionAccess);
  }

  @Test
  @DisplayName("Initial render displays flashcard nodes.")
  void initialRenderDisplaysFlashcardNodes() {
    waitForFxEvents();
    FxAssert.verifyThat("#flashcard-number-text", TextMatchers.hasText("1"));
    waitForFxEvents();
  }

  @Test
  @DisplayName("Can remove flashcard.")
  void canRemoveFlashcard() {
    int initialNodeCount = getFlashcardPaneContainerChildrenCount();

    clickOn("#remove-flashcard-button");
    waitForFxEvents();

    int currentNodeCount = getFlashcardPaneContainerChildrenCount();

    boolean nodeCountDecreasedOnButtonClick = (currentNodeCount == initialNodeCount - 1);

    waitForFxEvents();
    Assertions.assertTrue(nodeCountDecreasedOnButtonClick);
  }

  @Test
  @DisplayName("Can add flashcard.")
  void canAddFlashcard() {
    int initialNodeCount = getFlashcardPaneContainerChildrenCount();

    clickOn("#addFlashcardButton");
    waitForFxEvents();

    int currentNodeCount = getFlashcardPaneContainerChildrenCount();

    boolean nodeCountIncreasedOnButtonClick = (currentNodeCount == initialNodeCount + 1);

    waitForFxEvents();
    Assertions.assertTrue(nodeCountIncreasedOnButtonClick);
  }

  @Test
  @DisplayName("Can switch to Dashboard.")
  void canSwitchToDashboard() {
    waitForFxEvents();
    clickOn("#goHome");

    waitForFxEvents();
    // Check that we loaded Dashboard view
    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Dashboard"));
  }

  @Test
  @DisplayName("Invalid input gives correct feedback.")
  void invalidInputGivesCorrectFeedback() {
    // Invalid input
    waitForFxEvents();
    TestFxHelper.sleep(1);
    clickOn("#front-input").write("front");

    waitForFxEvents();
    TestFxHelper.sleep(1);
    clickOn("#answer-input").write("answer");

    waitForFxEvents();
    TestFxHelper.sleep(1);
    verifyInputData("", "description", true);

    waitForFxEvents();
    TestFxHelper.sleep(1);
    clearInputData("#name");

    waitForFxEvents();
    TestFxHelper.sleep(1);
    clearInputData("#description");

    // Description can be empty
    waitForFxEvents();
    TestFxHelper.sleep(1);
    verifyInputData("name", "", false);
  }

  private void clearInputData(String nodeId) {
    doubleClickOn(nodeId).push(KeyCode.BACK_SPACE);
  }

  /**
   * A helper method for verifying input data.
   *
   * @param name           is the provided name in the input form
   * @param description    is the provided description in the input form
   * @param isErrorMessage determines if the feedback is an error message or not.
   */
  private void verifyInputData(String name, String description, boolean isErrorMessage) {
    // Input data into UI
    waitForFxEvents();
    clickOn("#name").write(name);
    waitForFxEvents();
    clickOn("#description").write(description);
    waitForFxEvents();
    clickOn("#storeQuizButton");

    String feedback = isErrorMessage ? quizController.getFeedbackErrorMessage()
            : quizController.getFeedbackSuccessMessage();

    // Validate that user got correct feedback in UI
    waitForFxEvents();
    FxAssert.verifyThat("#feedback", LabeledMatchers.hasText(feedback));
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

  private int getFlashcardPaneContainerChildrenCount() {
    VBox container = lookup("#flashcardPaneContainer").query();
    return container.getChildren().size();
  }
}

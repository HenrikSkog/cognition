package ui;

import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
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
import ui.controllers.QuizController;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

public class QuizTest extends ApplicationTest {
  private final String validUsername = "valid-username";
  private final String validPassword = "valid-password";
  private Scene scene;
  private QuizController quizController;
  private CognitionStorage cognitionStorage;


  @AfterEach
  void tearDown() {
    clearUserStorage();
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = getLoader("Quiz");

    CognitionStorage cognitionStorage = new CognitionStorage("cognitionTest.json");
    this.cognitionStorage = cognitionStorage;

    // In the app, there is no logical way for Create Quiz to be accessed without a
    // logged-in user. Thus, we create a fake user here to emulate it
    User loggedInUser = new User(UUID.randomUUID().toString(), validUsername, validPassword);

    cognitionStorage.create(loggedInUser);

    quizController = new QuizController(loggedInUser, cognitionStorage);
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
    Assertions.assertNotNull(cognitionStorage);
  }

  @Test
  @DisplayName("Initial render displays flashcard nodes.")
  void initialRenderDisplaysFlashcardNodes() {
    FxAssert.verifyThat("#flashcard-number-text", TextMatchers.hasText("1"));
  }

  @Test
  @DisplayName("Can remove flashcard.")
  void canRemoveFlashcard() {
    int initialNodeCount = quizController.getFlashcardPaneContainer().getChildren().size();

    clickOn("#remove-flashcard-button");

    int currentNodeCount = quizController.getFlashcardPaneContainer().getChildren().size();

    boolean nodeCountDecreasedOnButtonClick = (currentNodeCount == initialNodeCount - 1);

    Assertions.assertTrue(nodeCountDecreasedOnButtonClick);
  }

  @Test
  @DisplayName("Can add flashcard.")
  void canAddFlashcard() {
    int initialNodeCount = quizController.getFlashcardPaneContainer().getChildren().size();

    clickOn("#addFlashcardButton");

    int currentNodeCount = quizController.getFlashcardPaneContainer().getChildren().size();

    boolean nodeCountIncreasedOnButtonClick = (currentNodeCount == initialNodeCount + 1);


    Assertions.assertTrue(nodeCountIncreasedOnButtonClick);
  }

  @Test
  @DisplayName("Can switch to Dashboard.")
  void canSwitchToDashboard() {
    clickOn("#goToDashboardButton");

    // Check that we loaded Dashboard view
    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Dashboard"));
  }

  @Test
  @DisplayName("Invalid input gives correct feedback.")
  void invalidInputGivesCorrectFeedback() {
    // Invalid input
    clickOn("#front-input").write("front");
    clickOn("#answer-input").write("answer");
    verifyInputData("", "description", true);

    clearInputData("#name");
    clearInputData("#description");

    // Description can be empty
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
    clickOn("#name").write(name);
    clickOn("#description").write(description);
    clickOn("#storeQuizButton");

    String feedback = isErrorMessage ? quizController.getFeedbackErrorMessage()
            : quizController.getFeedbackSuccessMessage();

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

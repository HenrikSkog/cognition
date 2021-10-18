package ui;

import core.Flashcard;
import core.Quiz;
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
import ui.controllers.QuizController;
import ui.controllers.annotations.SuppressFBWarnings;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

public class CreateQuizTest extends ApplicationTest {
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

    // in the app there is no logical way for Create Quiz to be accessed without a
    // logged in user. Thus, we create a fake user here to emulate it
    User loggedInUser = new User(UUID.randomUUID().toString(), validUsername, validPassword);

    cognitionStorage.create(loggedInUser);

    quizController = new QuizController(loggedInUser, cognitionStorage);
    // IMPORTANT: We do not set a quiz object here. Thus, we render the view like we
    // create a quiz.
    loader.setController(quizController);

    scene = new Scene(loader.load());
    stage.setScene(scene);
    stage.show();
  }

  @SuppressFBWarnings
  private FXMLLoader getLoader(String fxml) {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("views/" + fxml + ".fxml"));
    return loader;
  }

  @Test
  @DisplayName("Can create quiz.")
  void canCreateQuiz() {
    // Define input values
    String name = "name";
    String description = "description";
    String front = "front";
    String answer = "answer";

    // Add flashcard
    clickOn("#front-input").write(front);
    clickOn("#answer-input").write(answer);

    // Create quiz
    verifyInputData("name", "description", false);

    List<Quiz> quizzes = new ArrayList<>();

    try {
      quizzes = cognitionStorage.readByUsername(validUsername).getQuizzes();
    } catch (IOException e) {
      fail();
    }

    boolean quizWasStored = false;

    // Note that we do not compare UUID in this test,
    // as a new UUID is generated on button click in the UI regardless.
    for (Quiz quiz : quizzes) {
      boolean quizMatches = quiz.getName().equals(name) && quiz.getDescription().equals(description);

      if (quizMatches) {
        for (Flashcard flashcard : quiz.getFlashcards()) {
          boolean flashcardMatches = flashcard.getFront().equals(front) && flashcard.getAnswer().equals(answer);

          if (flashcardMatches) {
            quizWasStored = true;
            break;
          }
        }
      }
    }

    Assertions.assertTrue(quizWasStored);
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

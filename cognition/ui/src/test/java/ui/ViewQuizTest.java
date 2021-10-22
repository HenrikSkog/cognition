package ui;

import core.Flashcard;
import core.Quiz;
import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import json.CognitionStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextMatchers;
import ui.controllers.ViewQuizController;
import ui.controllers.annotations.SuppressFBWarnings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

public class ViewQuizTest extends ApplicationTest {

  private final String validUsername = "valid-username";
  private final String validPassword = "valid-password";
  private Scene scene;
  private ViewQuizController viewQuizController;
  private List<Flashcard> flashcards = new ArrayList<>();

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = getLoader("ViewQuiz");

    CognitionStorage cognitionStorage = new CognitionStorage("cognitionTest.json");
    // in the app there is no logical way for Create Quiz to be accessed without a logged in user. Thus, we create a fake user here to emulate it
    User loggedInUser = new User(UUID.randomUUID().toString(), validUsername, validPassword);

    cognitionStorage.create(loggedInUser);

    Quiz quiz = new Quiz(UUID.randomUUID().toString(), "Test quiz",
        "This is a test quiz used for development purposes");
    quiz.addFlashcard(new Flashcard(UUID.randomUUID().toString(), "What is the capital of Spain?", "Madrid"));
    quiz.addFlashcard(
        new Flashcard(UUID.randomUUID().toString(), "What is the largest desert in the world?", "Antarctica"));

    flashcards = quiz.getFlashcards();

    viewQuizController = new ViewQuizController(loggedInUser, quiz, cognitionStorage);
    loader.setController(viewQuizController);

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
  @DisplayName("Can load quiz")
  void canLoadQuiz() {
    FxAssert.verifyThat("#flashcardText", TextMatchers.hasText("What is the capital of Spain?"));
  }

  @Test
  @DisplayName("Validates correct answer")
  void validatesCorrectAnswer() {

    FxAssert.verifyThat("#flashcardText", TextMatchers.hasText(flashcards.get(0).getFront()));

    clickOn("#answerInput").write(flashcards.get(0).getAnswer());
    clickOn("#submitAnswer");

    FxAssert.verifyThat("#feedback", LabeledMatchers.hasText("Correct answer!"));

  }

  @Test
  @DisplayName("No input gives error")
  void noInputGivesError() {

    FxAssert.verifyThat("#flashcardText", TextMatchers.hasText(flashcards.get(0).getFront()));

    clickOn("#submitAnswer");

    FxAssert.verifyThat("#feedback", LabeledMatchers.hasText("Please provide an answer."));

  }

  @Test
  @DisplayName("Invalid answer gives error")
  void invalidAnswerGivesError() {

    FxAssert.verifyThat("#flashcardText", TextMatchers.hasText(flashcards.get(0).getFront()));

    clickOn("#answerInput").write(flashcards.get(1).getAnswer());
    clickOn("#submitAnswer");

    FxAssert.verifyThat("#feedback", LabeledMatchers
        .hasText("Incorrect! \n The correct answer was: " + flashcards.get(0).getAnswer().toLowerCase() + "."));

  }

  @Test
  @DisplayName("Set quiz does not throw")
  void setQuizDoesNotThrow() {

    Quiz quiz = new Quiz(UUID.randomUUID().toString(), "Test quiz",
        "This is a test quiz used for development purposes");
    quiz.addFlashcard(new Flashcard(UUID.randomUUID().toString(), "What is the capital of Spain?", "Madrid"));
    quiz.addFlashcard(
        new Flashcard(UUID.randomUUID().toString(), "What is the largest desert in the world?", "Antarctica"));

    try {
      viewQuizController.setQuiz(quiz);
    } catch (Exception e) {
      fail();
    }

  }

  @Test
  @DisplayName("Run quiz with wrong answers and give correct number of right answers")
  void runQuizWithFails() {
    // answer first question incorrectly
    FxAssert.verifyThat("#flashcardText", TextMatchers.hasText("What is the capital of Spain?"));

    clickOn("#answerInput").write("Wrong answer");
    clickOn("#submitAnswer");

    // answer with the correct answer
    clickOn("#answerInput").write(flashcards.get(0).getAnswer());
    clickOn("#submitAnswer");

    // answer rest of flashcards correctly
    for (Flashcard flashcard : flashcards.subList(1, flashcards.size())) {
      clickOn("#answerInput").write(flashcard.getAnswer());
      clickOn("#submitAnswer");
    }

    // assert that you got max - 1 correct answers
    FxAssert.verifyThat("#flashcardText",
        TextMatchers.hasText(
            "End of quiz! " + "You got " + (flashcards.size() - 1) + " right " +
                "out " + "of " + flashcards.size() + " possible."));
  }

  @Test
  @DisplayName("Finish quiz")
  void finishQuiz() {

    for (Flashcard flashcard : flashcards) {
      FxAssert.verifyThat("#flashcardText", TextMatchers.hasText(flashcard.getFront()));

      clickOn("#answerInput").write(flashcard.getAnswer());
      clickOn("#submitAnswer");

    }

    FxAssert.verifyThat("#flashcardText", TextMatchers.hasText(
        "End of quiz! " + "You got " + flashcards.size() + " right out of " + flashcards.size() + " possible."));
  }

  @Test
  @DisplayName("Can navigate to Dashboard")
  void canNavigateToDashboard() {

    clickOn("#goHome");

    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Dashboard"));
  }

  @Test
  @DisplayName("Controller is defined.")
  void controllerIsDefined() {
    Assertions.assertNotNull(viewQuizController);
  }

  @Test
  @DisplayName("Can navigate to Create Quiz")
  void canNavigateToCreateQuiz() {

    clickOn("#createQuizButton");

    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Quiz"));

  }


}

package ui;

import core.Flashcard;
import core.Quiz;
import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextMatchers;
import java.util.ArrayList;
import java.util.List;

import static core.tools.Tools.createUuid;
import static org.junit.jupiter.api.Assertions.fail;
import static ui.TestFxHelper.waitForFxEvents;

public class ViewQuizTest extends ApplicationTest {

  private final String validUsername = "valid-username";
  private final String validPassword = "valid-password";
  private Scene scene;
  private ViewQuizController viewQuizController;
  private List<Flashcard> flashcards = new ArrayList<>();

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = getLoader("ViewQuiz");

    RemoteCognitionAccess remoteCognitionAccess = new RemoteCognitionAccess(AppTest.TEST_PORT);
    // in the app there is no logical way for Create Quiz to be accessed without a logged in user. Thus, we create a fake user here to emulate it
    User loggedInUser = new User(validUsername, validPassword);

    remoteCognitionAccess.create(loggedInUser);

    Quiz quiz = new Quiz(createUuid(), "Test quiz",
            "This is a test quiz used for development purposes");
    quiz.addFlashcard(new Flashcard(createUuid(), "What is the capital of Spain?", "Madrid"));
    quiz.addFlashcard(
            new Flashcard(createUuid(), "What is the largest desert in the world?", "Antarctica"));

    flashcards = quiz.getFlashcards();

    viewQuizController = new ViewQuizController(loggedInUser, quiz, remoteCognitionAccess);
    loader.setController(viewQuizController);

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
  @DisplayName("Can load quiz")
  void canLoadQuiz() {
    waitForFxEvents();
    FxAssert.verifyThat("#flashcardText", TextMatchers.hasText("What is the capital of Spain?"));
  }

  @Test
  @DisplayName("Validates correct answer")
  void validatesCorrectAnswer() {
    waitForFxEvents();
    FxAssert.verifyThat("#flashcardText", TextMatchers.hasText(flashcards.get(0).getFront()));

    waitForFxEvents();
    clickOn("#answerInput").write(flashcards.get(0).getAnswer());
    waitForFxEvents();
    clickOn("#submitAnswer");

    waitForFxEvents();
    FxAssert.verifyThat("#feedback", LabeledMatchers.hasText("Correct answer!"));

  }

  @Test
  @DisplayName("No input gives error")
  void noInputGivesError() {
    waitForFxEvents();
    FxAssert.verifyThat("#flashcardText", TextMatchers.hasText(flashcards.get(0).getFront()));

    waitForFxEvents();
    clickOn("#submitAnswer");

    waitForFxEvents();
    FxAssert.verifyThat("#feedback", LabeledMatchers.hasText("Please provide an answer."));
  }

  @Test
  @DisplayName("Invalid answer gives error")
  void invalidAnswerGivesError() {
    waitForFxEvents();
    FxAssert.verifyThat("#flashcardText", TextMatchers.hasText(flashcards.get(0).getFront()));

    waitForFxEvents();
    clickOn("#answerInput").write(flashcards.get(1).getAnswer());

    waitForFxEvents();
    clickOn("#submitAnswer");

    waitForFxEvents();
    FxAssert.verifyThat("#feedback", LabeledMatchers
            .hasText("Incorrect! \n The correct answer was: " + flashcards.get(0).getAnswer().toLowerCase() + "."));

  }

  @Test
  @DisplayName("Set quiz does not throw")
  void setQuizDoesNotThrow() {

    Quiz quiz = new Quiz(createUuid(), "Test quiz",
            "This is a test quiz used for development purposes");
    quiz.addFlashcard(new Flashcard(createUuid(), "What is the capital of Spain?", "Madrid"));
    quiz.addFlashcard(
            new Flashcard(createUuid(), "What is the largest desert in the world?", "Antarctica"));

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
    waitForFxEvents();
    FxAssert.verifyThat("#flashcardText", TextMatchers.hasText("What is the capital of Spain?"));

    waitForFxEvents();
    clickOn("#answerInput").write("Wrong answer");
    waitForFxEvents();
    clickOn("#submitAnswer");

    // answer with the correct answer
    waitForFxEvents();
    clickOn("#answerInput").write(flashcards.get(0).getAnswer());
    waitForFxEvents();
    clickOn("#submitAnswer");

    // answer rest of flashcards correctly
    for (Flashcard flashcard : flashcards.subList(1, flashcards.size())) {
      waitForFxEvents();
      clickOn("#answerInput").write(flashcard.getAnswer());
      waitForFxEvents();
      clickOn("#submitAnswer");
    }

    waitForFxEvents();
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
      waitForFxEvents();
      FxAssert.verifyThat("#flashcardText", TextMatchers.hasText(flashcard.getFront()));

      waitForFxEvents();
      clickOn("#answerInput").write(flashcard.getAnswer());
      waitForFxEvents();
      clickOn("#submitAnswer");

    }

    waitForFxEvents();
    FxAssert.verifyThat("#flashcardText", TextMatchers.hasText(
            "End of quiz! " + "You got " + flashcards.size() + " right out of " + flashcards.size() + " possible."));
  }

  @Test
  @DisplayName("Can navigate to Dashboard")
  void canNavigateToDashboard() {
    waitForFxEvents();
    clickOn("#goHome");

    waitForFxEvents();
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
    waitForFxEvents();
    clickOn("#createQuizButton");

    waitForFxEvents();
    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Quiz"));

  }

  @Test
  @DisplayName("When showing answer: Does not show answer on first click.")
  void whenShowingAnswer_doesNotShowAnswerOnFirstClick() {
    waitForFxEvents();
    clickOn("#showAnswer");

    waitForFxEvents();
    FxAssert.verifyThat("#answerText", TextMatchers.hasText("Don't give up before trying!"));
  }

  @Test
  @DisplayName("When showing answer: First click after trying shows answer.")
  void whenShowingAnswer_firstClickAfterTryingShowsAnswer() {
    // click a first time to show the answer
    waitForFxEvents();
    clickOn("#answerInput").write("wrong-answer");
    waitForFxEvents();
    clickOn("#submitAnswer");
    waitForFxEvents();
    clickOn("#showAnswer");

    waitForFxEvents();
    FxAssert.verifyThat("#answerText", TextMatchers.hasText("The correct answer is: " + flashcards.get(0).getAnswer()));
  }

  @Test
  @DisplayName("Can hide answer")
  void canHideAnswer() {
    // click a first time to show the answer
    waitForFxEvents();
    clickOn("#answerInput").write("wrong-answer");
    waitForFxEvents();
    clickOn("#submitAnswer");
    waitForFxEvents();
    clickOn("#showAnswer");
    // click a second time to hide it
    waitForFxEvents();
    clickOn("#showAnswer");

    waitForFxEvents();
    FxAssert.verifyThat("#answerText", TextMatchers.hasText(""));
  }
}

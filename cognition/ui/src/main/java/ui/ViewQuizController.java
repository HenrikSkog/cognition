package ui;

import core.Flashcard;
import core.Quiz;
import core.User;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * ViewQuizController is responsible for the presentation logic in the ViewQuiz view.
 */
public class ViewQuizController extends LoggedInController {

  private Quiz quiz;
  private List<Flashcard> flashcards;
  private int flashcardIndex = 0;
  private int correctAnswerCount = 0;
  private boolean hasFailedFlashcard = false;

  @FXML
  private TextField answerInput;

  @FXML
  private Text heading;

  @FXML
  private Text flashcardText;

  @FXML
  private Text description;

  @FXML
  private Text answerText;

  public ViewQuizController(User user, Quiz quiz, RemoteCognitionAccess remoteCognitionAccess) {
    super(user, remoteCognitionAccess);
    this.quiz = quiz;
  }

  @FXML
  protected void initialize() {
    flashcards = this.quiz.getFlashcards();

    heading.setText(quiz.getName());
    description.setText(quiz.getDescription());

    // Initialize the first flashcard
    flashcardText.setText(flashcards.get(flashcardIndex).getFront());
  }

  /**
   * Updates the current flashcard when the user is doing a quiz.
   */
  private void nextFlashcard() {
    if (flashcardIndex == flashcards.size() - 1) {
      flashcardText.setText(
              new StringBuilder()
                      .append("End of quiz! ")
                      .append("You got ")
                      .append(correctAnswerCount)
                      .append(" right out of ")
                      .append(flashcards.size())
                      .append(" possible.")
                      .toString());
      setAnswerText("");
      return;
    }

    flashcardIndex++;
    flashcardText.setText(flashcards.get(flashcardIndex).getFront());
    setAnswerText("");
  }

  /**
   * Triggered when an answer for a flashcard is provided. Gives feedback based on if the answer
   * was right or not, and increments the number of right answers accordingly.
   */
  @FXML
  private void checkAnswer() {
    String userAnswer = answerInput.getText().toLowerCase();
    String correctAnswer = flashcards.get(flashcardIndex).getAnswer().toLowerCase();

    if (answerInput.getText().equals("")) {
      setFeedbackErrorMode(true);
      setFeedbackText("Please provide an answer.");
      return;
    }

    if (!userAnswer.equals(correctAnswer)) {
      setFeedbackErrorMode(true);
      setFeedbackText("Incorrect! \n The correct answer was: " + correctAnswer + ".");
      hasFailedFlashcard = true;
      answerInput.setText("");
    } else {
      if (!hasFailedFlashcard) {
        correctAnswerCount++;
      }
      setFeedbackErrorMode(false);
      setFeedbackText("Correct answer!");
      answerInput.setText("");

      hasFailedFlashcard = false;
      nextFlashcard();
    }
  }

  /**
   * Sets the styling on feedback in the frontend.
   *
   * @param mode is a boolean representation of the feedback mode.
   */
  private void setFeedbackErrorMode(boolean mode) {
    if (mode) {
      setFeedbackStyle("-fx-text-fill: red");
    } else {
      setFeedbackStyle("-fx-text-fill: green");
    }
  }

  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
  }

  @FXML
  private void showAnswer() {
    String invalidText = "Don't give up before trying!";
    if (hasFailedFlashcard) {
      String correctAnswer = flashcards.get(flashcardIndex).getAnswer();
      if (answerText.getText().equals("") || answerText.getText().equals(invalidText)) {
        setAnswerText("The correct answer is: " + correctAnswer);
      } else {
        setAnswerText("");
      }
    } else {
      setAnswerText(invalidText);
    }
  }


  private void setAnswerText(String text) {
    this.answerText.setText(text);
  }
}

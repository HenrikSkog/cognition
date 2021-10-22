package ui.controllers;

import core.Flashcard;
import core.Quiz;
import core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import json.CognitionStorage;
import java.util.List;

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
  private Labeled feedback;

  @FXML
  private TextField answerInput;

  @FXML
  private Text heading;

  @FXML
  private Text flashcardText;

  @FXML
  private Text description;

  public ViewQuizController(User user, Quiz quiz, CognitionStorage cognitionStorage) {
    super(user, cognitionStorage);
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
  public void nextFlashcard() {
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
      return;
    }

    flashcardIndex++;
    flashcardText.setText(flashcards.get(flashcardIndex).getFront());
  }

  /**
   * Triggered when an answer for a flashcard is provided. Gives feedback based on if the answer
   * was right or not, and increments the number of right answers accordingly.
   */
  @FXML
  public void checkAnswer() {
    String userAnswer = answerInput.getText().toLowerCase();
    String correctAnswer = flashcards.get(flashcardIndex).getAnswer().toLowerCase();

    if (answerInput.getText().equals("")) {
      setFeedbackErrorMode(true);
      feedback.setText("Please provide an answer.");
      return;
    }

    if (!userAnswer.equals(correctAnswer)) {
      setFeedbackErrorMode(true);
      feedback.setText("Incorrect! \n The correct answer was: " + correctAnswer + ".");
      hasFailedFlashcard = true;
      answerInput.setText("");
    } else {
      if (!hasFailedFlashcard) {
        correctAnswerCount++;
      }
      setFeedbackErrorMode(false);
      feedback.setText("Correct answer!");
      answerInput.setText("");

      hasFailedFlashcard = false;
      nextFlashcard();
    }
  }

  /**
   * Triggered when switching to the Dashboard view.
   *
   * @param event is the ActionEvent on button click.
   */
  @FXML
  public void handleDashboard(ActionEvent event) {
    changeToView(event, new DashboardController(getUser(), getCognitionStorage()),
        "Dashboard", feedback);
  }

  @FXML
  public void handleMyQuizzes(ActionEvent event) {
    changeToView(event, new MyQuizzesController(getUser(), getCognitionStorage()),
        "MyQuizzes", feedback);
  }

  @FXML
  public void handleCreateQuiz(ActionEvent event) {
    changeToView(event, new QuizController(getUser(), getCognitionStorage()),
        "Quiz", feedback);
  }

  /**
   * Sets the styling on feedback in the frontend.
   *
   * @param mode is a boolean representation of the feedback mode.
   */
  private void setFeedbackErrorMode(boolean mode) {
    if (mode) {
      feedback.setStyle("-fx-text-fill: red");
    } else {
      feedback.setStyle("-fx-text-fill: green");
    }
  }

  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
  }
}

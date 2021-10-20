package ui.controllers;

import core.Quiz;
import core.User;
import java.io.IOException;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import json.CognitionStorage;
import ui.controllers.annotations.SuppressFBWarnings;

/**
 * MyQuizzesController is responsible for handling the
 * presentation logic in the "My Quizzes" view.
 */
public class MyQuizzesController extends LoggedInController {

  @SuppressFBWarnings
  public Label feedback;

  @SuppressFBWarnings
  public ListView<String> quizzesListView;
  private Quiz selectedQuiz;
  private String feedbackErrorMessage;

  public MyQuizzesController(User user, CognitionStorage cognitionStorage) {
    super(user, cognitionStorage);
  }

  /**
   * Renders the initial view.
   */
  @FXML
  public void initialize() {
    ObservableList<String> displayedQuizzes = FXCollections
            .observableArrayList(
                    getUser().getQuizzes().stream().map(Quiz::getName)
                            .collect(Collectors.toList()));

    quizzesListView.setItems(displayedQuizzes);

    quizzesListView.setOnMouseClicked(event -> {
      int index = quizzesListView.getSelectionModel().getSelectedIndex();
      // Prevents IndexOutOfBoundsException if invalid element is selected
      if (index != -1) {
        selectedQuiz = getUser().getQuizzes().get(index);
      }
    });

  }

  /**
   * Triggered when user clicks to start quiz.
   *
   * @param event is the ActionEvent on button click.
   */
  @FXML
  public void handleStartQuiz(ActionEvent event) {
    if (quizIsNotSelected()) {
      return;
    }

    // Set state in controller
    ViewQuizController viewQuizController = new ViewQuizController(
            getUser(),
            selectedQuiz,
            getCognitionStorage()
    );
    viewQuizController.setQuiz(selectedQuiz);

    changeToView(event, viewQuizController, "ViewQuiz", feedback);

  }

  /**
   * Triggered when there is a quiz to be updated.
   *
   * @param event is the ActionEvent on button click.
   */
  @FXML
  public void handleUpdateQuiz(ActionEvent event) {
    if (quizIsNotSelected()) {
      return;
    }

    // Set state in controller
    QuizController quizController = new QuizController(getUser(), getCognitionStorage());
    quizController.setQuiz(selectedQuiz);

    changeToView(event, quizController, "Quiz", feedback);
  }

  private boolean quizIsNotSelected() {
    if (this.selectedQuiz == null) {
      feedbackErrorMessage = "No selected quiz";
      feedback.setText(feedbackErrorMessage);
      return true;
    }
    return false;
  }

  /**
   * Gets triggered when a quiz is to be deleted.
   *
   * @param actionEvent is the ActionEvent on button click.
   */
  @FXML
  public void handleDeleteQuiz(ActionEvent actionEvent) {
    if (quizIsNotSelected()) {
      return;
    }

    quizzesListView.setOnMouseClicked(event -> {
      int index = quizzesListView.getSelectionModel().getSelectedIndex();

      // Prevents IndexOutOfBoundsException
      if (index != -1) {
        getUser().getQuizzes().remove(index);

        try {
          getCognitionStorage().update(getUser().getUuid(), getUser());

        } catch (IOException e) {
          feedback.setText("An error occurred when trying to delete selected quiz.");
        }
        initialize();
      }
    });

  }

  @FXML
  public void handleCreateQuiz(ActionEvent event) {
    changeToView(event, new QuizController(getUser(), getCognitionStorage()),
            "Quiz", feedback);
  }

  @FXML
  public void handleLogout(ActionEvent event) {
    changeToView(event, new LoginController(getCognitionStorage()),
            "Login", feedback);
  }

  public String getFeedbackErrorMessage() {
    return feedbackErrorMessage;
  }

  @FXML
  public void handleDashboard(ActionEvent event) {
    changeToView(event, new DashboardController(getUser(), getCognitionStorage()),
            "Dashboard", feedback);
  }

  public ListView getListView() {
    return quizzesListView;
  }
}

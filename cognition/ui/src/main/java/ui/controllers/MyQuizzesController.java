package ui.controllers;

import core.Quiz;
import core.User;
import java.io.IOException;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import ui.RemoteCognitionAccess;

/**
 * MyQuizzesController is responsible for handling the
 * presentation logic in the "My Quizzes" view.
 */
public class MyQuizzesController extends LoggedInController {

  @FXML
  private ListView<Quiz> quizzesListView;

  @FXML
  private TextField searchInput;

  private Quiz selectedQuiz;
  private String feedbackErrorMessage;

  public MyQuizzesController(User user, RemoteCognitionAccess remoteCognitionAccess) {
    super(user, remoteCognitionAccess);
  }

  /**
   * Renders the initial view.
   */
  @FXML
  public void initialize() {
    // Initially render ListView
    quizzesListView.setItems(getQuizzes(""));
    setupListView();

    // Add onChange event handler for search input
    searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
      quizzesListView.setItems(getQuizzes(newValue));
    });

    // Update selected quiz by what quiz is selected in the ui
    quizzesListView.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> this.selectedQuiz = newValue);
  }

  /**
   * Sets up list view cell factory so listview properly works using Quiz as type.
   */
  private void setupListView() {
    quizzesListView.setCellFactory(quizListView -> new ListCell<>() {
      @Override
      protected void updateItem(Quiz item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
          setText(null);
          setGraphic(null);
        } else {
          setText(item.getName());
        }
      }
    });
  }

  /**
   * Compares the users quizzes with an input and returns the matching quizzes. A comparison
   * functions by checking if a name contains the given input.
   *
   * @param input the string quizzes are matched up against
   * @return an ObservableList with all the matching quizzes
   */
  private ObservableList<Quiz> getQuizzes(String input) {
    var stream = getUser().getQuizzes().stream();

    if (!input.equals("")) {
      stream = stream.filter(quiz -> quiz.getName().toLowerCase().contains(input.toLowerCase()));
    }

    return FXCollections.observableArrayList(stream.collect(Collectors.toList()));
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
            getCognitionAccess()
    );
    viewQuizController.setQuiz(selectedQuiz);

    changeToView(event, viewQuizController, "ViewQuiz");

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
    QuizController quizController = new QuizController(getUser(), getCognitionAccess());
    quizController.setQuizBeingUpdated(selectedQuiz);

    changeToView(event, quizController, "Quiz");
  }

  private boolean quizIsNotSelected() {
    if (this.selectedQuiz == null) {
      feedbackErrorMessage = "No selected quiz";
      setFeedbackText(feedbackErrorMessage);
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

    // update java state
    getUser().removeQuiz(selectedQuiz);
    this.selectedQuiz = null;

    // update ui state
    quizzesListView.getItems().remove(quizzesListView.getSelectionModel().getSelectedItem());
    ObservableList<Quiz> quizzes = quizzesListView.getItems();
    quizzesListView.setItems(quizzes);

    // update local storage state
    try {
      getCognitionAccess().update(getUser());

    } catch (IOException | InterruptedException e) {
      setFeedbackText("An error occurred when trying to delete selected quiz.");
    }
  }

  public String getFeedbackErrorMessage() {
    return feedbackErrorMessage;
  }

  public ListView getListView() {
    return quizzesListView;
  }
}

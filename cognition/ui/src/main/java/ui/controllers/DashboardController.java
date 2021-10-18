package ui.controllers;

import core.User;
import core.tools.Tools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import json.CognitionStorage;

/**
 * DashboardController has the presentation logic for the DashBoard view.
 */
public class DashboardController extends LoggedInController {

  @FXML
  public Labeled heading;
  @FXML
  private ListView<String> flashcardPane;

  @FXML
  private Label feedback;

  public DashboardController(User user, CognitionStorage cognitionStorage) {
    super(user, cognitionStorage);
  }

  @FXML
  protected void initialize() {
    heading.setText("Welcome, " + Tools.capitalize(getUser().getUsername()));
  }

  @FXML
  public void handleLogout(ActionEvent event) {
    changeToView(event, new LoginController(getCognitionStorage()),
            "Login", feedback);
  }

  @FXML
  public void handleCreateQuiz(ActionEvent event) {
    changeToView(event, new QuizController(getUser(), getCognitionStorage()),
            "Quiz", feedback);
  }

  @FXML
  public void handleMyQuizzes(ActionEvent event) {
    changeToView(event, new MyQuizzesController(getUser(), getCognitionStorage()),
            "MyQuizzes", feedback);
  }
}

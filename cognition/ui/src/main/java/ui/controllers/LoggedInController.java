package ui.controllers;

import core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import json.CognitionStorage;

/**
 * LoggedInController holds state of the application when user
 * is logged in.
 * It extends Controller, as there's common functionality in these controllers.
 */
public abstract class LoggedInController extends Controller {

  private User user;

  public LoggedInController(User user, CognitionStorage cognitionStorage) {
    super(cognitionStorage);
    this.user = user;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @FXML
  public void handleLogout(ActionEvent event) {
    changeToView(event, new LoginController(getCognitionStorage()),
        "Login");
  }

  @FXML
  public void handleDashboard(ActionEvent event) {
    changeToView(event, new DashboardController(getUser(), getCognitionStorage()),
        "Dashboard");
  }

  @FXML
  public void handleMyQuizzes(ActionEvent event) {
    changeToView(event, new MyQuizzesController(getUser(), getCognitionStorage()),
        "MyQuizzes");
  }

  @FXML
  public void handleCreateQuiz(ActionEvent event) {
    changeToView(event, new QuizController(getUser(), getCognitionStorage()),
        "Quiz");
  }
}

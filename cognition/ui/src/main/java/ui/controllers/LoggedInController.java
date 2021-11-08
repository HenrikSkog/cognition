package ui.controllers;

import core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ui.RemoteCognitionAccess;

/**
 * LoggedInController holds state of the application when user
 * is logged in.
 * It extends Controller, as there's common functionality in these controllers.
 */
public abstract class LoggedInController extends Controller {

  private User user;

  public LoggedInController(User user, RemoteCognitionAccess remoteCognitionAccess) {
    super(remoteCognitionAccess);
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
    changeToView(event, new LoginController(getCognitionAccess()),
        "Login");
  }

  @FXML
  public void handleDashboard(ActionEvent event) {
    changeToView(event, new DashboardController(getUser(), getCognitionAccess()),
        "Dashboard");
  }

  @FXML
  public void handleMyQuizzes(ActionEvent event) {
    changeToView(event, new MyQuizzesController(getUser(), getCognitionAccess()),
        "MyQuizzes");
  }

  @FXML
  public void handleCreateQuiz(ActionEvent event) {
    changeToView(event, new QuizController(getUser(), getCognitionAccess()),
        "Quiz");
  }
}

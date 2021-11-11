package ui;

import core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

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

  /**
   * Handles logging out and going to the login screen
   *
   * @param event is the event given by javafx when the method is triggered
   */
  @FXML
  private void handleLogout(ActionEvent event) {
    changeToView(event, new LoginController(getCognitionAccess()),
            "Login");
  }

  /**
   * Handles going to the dashboard
   *
   * @param event is the event given by javafx when the method is triggered
   */
  @FXML
  private void handleDashboard(ActionEvent event) {
    changeToView(event, new DashboardController(getUser(), getCognitionAccess()),
            "Dashboard");
  }

  /**
   * Handles going to the "My Quizzes" view
   *
   * @param event is the event given by javafx when the method is triggered
   */
  @FXML
  private void handleMyQuizzes(ActionEvent event) {
    changeToView(event, new MyQuizzesController(getUser(), getCognitionAccess()),
            "MyQuizzes");
  }

  /**
   * Handles going to the "Create New Quiz" view
   *
   * @param event is the event given by javafx when the method is triggered
   */
  @FXML
  protected void handleCreateQuiz(ActionEvent event) {
    changeToView(event, new QuizController(getUser(), getCognitionAccess()),
            "Quiz");
  }
}

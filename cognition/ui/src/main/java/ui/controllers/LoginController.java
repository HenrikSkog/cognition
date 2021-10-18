package ui.controllers;

import core.User;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import json.CognitionStorage;

/**
 * LoginController is responsible for the presentation logic in the Login view.
 */
public class LoginController extends Controller {

  @FXML
  private Labeled feedback;
  @FXML
  private TextField usernameInput;
  @FXML
  private PasswordField passwordInput;

  private String feedbackErrorMessage = "No user with that username and password could be found.";

  public LoginController(CognitionStorage cognitionStorage) {
    super(cognitionStorage);
  }

  /**
   * Gets triggered when the user clicks the Login button.
   *
   * @param event is the ActionEvent on button click.
   */
  @FXML
  public void handleLogin(ActionEvent event) {
    String username = usernameInput.getText().toLowerCase();
    String password = passwordInput.getText();

    if (isValidLogin(username, password)) {
      goToDashboard(event, username);
    } else {
      feedback.setText(feedbackErrorMessage);
    }
  }

  /**
   * Validates the provided input.
   *
   * @param username is the String representation of the provided username
   * @param password is the String representation of the provided password
   * @return a boolean indicating whether the input is valid.
   */
  private boolean isValidLogin(String username, String password) {
    List<User> users;

    try {
      users = getCognitionStorage().readUsers();
    } catch (IOException e) {
      feedbackErrorMessage = "An error occurred when loading local storage.";
      return false;
    }

    if (users != null) {
      for (User user : users) {
        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
          return true;
        }
      }
    }

    feedbackErrorMessage = "No user with that username and password could be found.";
    return false;
  }

  /**
   * Gets triggered when the user clicks to go to Dashboard view.
   *
   * @param event is the ActionEvent on button click.
   * @param username is the username of the current user.
   */
  public void goToDashboard(ActionEvent event, String username) {
    User user;
    try {
      user = getCognitionStorage().readByUsername(username);
    } catch (IOException e) {
      feedbackErrorMessage = "An error occurred when reading the user from file";
      feedback.setText(feedbackErrorMessage);
      return;
    }

    changeToView(event, new DashboardController(user, getCognitionStorage()),
            "Dashboard", feedback);
  }

  @FXML
  public void goToRegister(ActionEvent event) {
    changeToView(event, new RegisterController(getCognitionStorage()),
            "Register", feedback);
  }

  /**
   * Gets the feedback error message. This method is useful during testing to
   * validate that the feedback the user receives is the correct feedback to
   * display.
   *
   * @return a String representation of the feedback error message.
   */
  public String getFeedbackErrorMessage() {
    return feedbackErrorMessage;
  }
}

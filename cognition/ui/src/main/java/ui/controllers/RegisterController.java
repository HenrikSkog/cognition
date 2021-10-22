package ui.controllers;

import core.User;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import json.CognitionStorage;

/**
 * RegisterController handles the presentation logic when registering a user.
 */
public class RegisterController extends Controller {
  /**
   * The default success message for feedback in the RegisterController. Will not
   * be updated, and is thus static. This field is used when testing. Hence, a
   * getter is needed.
   */
  private static final String feedbackSuccessMessage = "User created successfully!";
  @FXML
  private TextField usernameInput;
  @FXML
  private PasswordField passwordInput;
  @FXML
  private PasswordField passwordRepeatInput;
  @FXML
  private Labeled feedback;
  private String feedbackErrorMessage;

  public RegisterController(CognitionStorage cognitionStorage) {
    super(cognitionStorage);
  }

  /**
   * This method gets triggered when the user clicks on the button for registering a user.
   * Validates input, and registers user on valid input.
   * Gives the user a fitting feedback message if there's invalid input.
   */
  @FXML
  public void handleRegister() {
    String username = usernameInput.getText().toLowerCase();
    String password = passwordInput.getText();
    String passwordRepeat = passwordRepeatInput.getText();

    if (isValidRegister(username, password, passwordRepeat)) {
      registerUser(username, password);
    } else {
      setFeedbackErrorMode(true);
      feedback.setText(feedbackErrorMessage);
    }
  }

  /**
   * Validates the provided input.
   *
   * @param username       is the String representation of the provided username.
   * @param password       is the String representation of the provided password.
   * @param passwordRepeat is the String representation of the provided repeated
   *                       password.
   * @return a boolean indicating whether the input is valid.
   */
  private boolean isValidRegister(String username, String password, String passwordRepeat) {
    if (!User.isValidUsername(username)) {
      feedbackErrorMessage = "Username has to be at least 3 characters long";
      return false;
    }

    if (!User.isValidPassword(password)) {
      feedbackErrorMessage = "Password must be at least 6 characters long";
      return false;
    }

    if (!password.equals(passwordRepeat)) {
      feedbackErrorMessage = "Passwords do not match";
      return false;
    }

    List<User> users = null;
    try {
      users = getCognitionStorage().readUsers();
    } catch (IOException e) {
      feedbackErrorMessage = "An error occurred when reading from local storage.";
      feedback.setText(feedbackErrorMessage);
    }

    if (users != null) {
      for (User user : users) {
        if (user.getUsername().equals(username)) {
          feedbackErrorMessage = username + " is already in use.";
          return false;
        }
      }
    }

    return true;

  }

  private void registerUser(String username, String password) {
    try {
      getCognitionStorage().create(new User(UUID.randomUUID().toString(), username, password));
      setFeedbackErrorMode(false);
      feedback.setText(feedbackSuccessMessage);
    } catch (IOException e) {
      feedback.setText("An error occurred when writing to local storage.");
    }

  }

  /**
   * Triggered when the view switches to "Login" view.
   *
   * @param event is the ActionEvent on button click.
   */
  @FXML
  public void goToLogin(ActionEvent event) {
    changeToView(event, new LoginController(getCognitionStorage()),
            "Login");
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

  /**
   * Gets the feedback success message. This method is useful during testing to
   * validate that the feedback the user receives is the correct feedback to
   * display.
   *
   * @return a String representation of the feedback success message.
   */
  public String getFeedbackSuccessMessage() {
    return feedbackSuccessMessage;
  }

  private void setFeedbackErrorMode(boolean mode) {
    if (mode) {
      feedback.setStyle("-fx-text-fill: red");
    } else {
      feedback.setStyle("-fx-text-fill: green");
    }
  }

  @FXML
  protected void initialize() {
  }
}

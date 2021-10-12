package ui.controllers;

import core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class RegisterController extends Controller {
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private PasswordField passwordRepeatInput;
    @FXML
    private Labeled feedback;

    private String feedbackErrorMessage;

    /**
     * The default success message for feedback in the RegisterController.
     * Will not be updated, and is thus static.
     * This field is used when testing. Hence, a getter is needed.
     */
    private static final String feedbackSuccessMessage = "User created successfully!";

    @FXML
    public void handleRegister() {
        String username = usernameInput.getText().toLowerCase();
        String password = passwordInput.getText();
        String passwordRepeat = passwordRepeatInput.getText();

        if (isValidRegister(username, password, passwordRepeat)) {
            registerUser(username, password);
        } else {
            feedback.setStyle("-fx-text-fill: red");
            feedback.setText(feedbackErrorMessage);
        }
    }

    /**
     * Validates the provided input.
     *
     * @param username       is the String representation of the provided username.
     * @param password       is the String representation of the provided password.
     * @param passwordRepeat is the String representation of the provided repeated password.
     * @return a boolean indicating whether the input is valid.
     */
    private boolean isValidRegister(String username, String password, String passwordRepeat) {
        if (username.length() < 3) {
            feedbackErrorMessage = "Username has to be at least 3 characters long";
            return false;
        }

        if (password.length() < 6) {
            feedbackErrorMessage = "Password must be at least 6 characters long";
            return false;
        }

        if (!password.equals(passwordRepeat)) {
            feedbackErrorMessage = "Passwords do not match";
            return false;
        }

        List<User> users = null;
        try {
            users = getUserStorage().readUsers();
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
            getUserStorage().create(
                    new User(UUID.randomUUID().toString(), username, password)
            );
            feedback.setStyle("-fx-text-fill: green");
            feedback.setText(feedbackSuccessMessage);
        } catch (IOException e) {
            feedback.setText("An error occurred when writing to local storage.");
        }

    }

    @FXML
    public void goToLogin(ActionEvent event) {
        // Load FXML view
        FXMLLoader loader = getLoader("Login");

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            feedback.setText("An error occurred when trying to switch view.");
            return;
        }

        // Set state in controller
        LoginController loginController = loader.getController();
        loginController.setUserStorage(getUserStorage());

        // Switch stage
        Stage stage = getStage(event);
        try {
            switchScene(stage, root);
        } catch (IOException e) {
            feedback.setText("An error occurred when trying to switch view.");
        }
    }

    /**
     * Gets the feedback error message.
     * This method is useful during testing to validate that the feedback the user
     * receives is the correct feedback to display.
     *
     * @return a String representation of the feedback error message.
     */
    public String getFeedbackErrorMessage() {
        return feedbackErrorMessage;
    }

    /**
     * Gets the feedback success message.
     * This method is useful during testing to validate that the feedback the user
     * receives is the correct feedback to display.
     *
     * @return a String representation of the feedback success message.
     */
    public String getFeedbackSuccessMessage() {
        return feedbackSuccessMessage;
    }

}

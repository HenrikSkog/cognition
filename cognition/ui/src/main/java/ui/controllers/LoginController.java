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

public class LoginController extends Controller {

    @FXML
    private Labeled feedback;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;

    private String feedbackErrorMessage = "No user with that username and password could be found.";

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
            users = getUserStorage().readUsers();
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


    public void goToDashboard(ActionEvent event, String username) {
        // Load FXML view
        FXMLLoader loader = getLoader("Dashboard");

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            feedbackErrorMessage = "An error occurred when trying to go to Dashboard.";
            feedback.setText(feedbackErrorMessage);
            return;
        }

        // Set state in controller
        DashboardController dashboardController = loader.getController();
        dashboardController.setUserStorage(getUserStorage());
        dashboardController.setUser(username);

        // Switch stage
        Stage stage = getStage(event);
        try {
            switchScene(stage, root);
        } catch (IOException e) {
            feedback.setText("An error occurred when trying to switch view.");
        }
    }

    @FXML
    public void goToRegister(ActionEvent event) {
        // Load FXML view
        FXMLLoader loader = getLoader("Register");

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            feedback.setText("An error occurred when trying to switch view.");
            return;
        }

        // Set state in controller
        RegisterController registerController = loader.getController();
        registerController.setUserStorage(getUserStorage());

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
}

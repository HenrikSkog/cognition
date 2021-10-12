package ui.controllers;

import javafx.event.ActionEvent;
import core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import json.UserStorage;
import ui.App;

import java.io.IOException;

public class DashboardController extends Controller {
    @FXML
    private ListView<String> flashcardPane;

    @FXML
    private Label feedback;

    @FXML
    private Labeled heading;

    /**
     * The currently logged-in user.
     */
    private User user;

    @FXML
    public void handleLogout(ActionEvent event) {
        // Load FXML view
        FXMLLoader loader = getLoader("Login");

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            feedback.setText("An error occurred when trying to log out.");
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
            feedback.setText("An error occurred when trying to log out.");
        }
    }

    @FXML
    public void handleCreateQuiz(ActionEvent event) {
        // Load FXML view
        FXMLLoader loader = getLoader("CreateQuiz");

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            feedback.setText("An error occurred when trying to create quiz.");
            return;
        }

        // Set state in controller
        CreateQuizController createQuizController = loader.getController();
        createQuizController.setUserStorage(getUserStorage());

        // Switch stage
        Stage stage = getStage(event);
        try {
            switchScene(stage, root);
        } catch (IOException e) {
            feedback.setText("An error occurred when trying to create quiz.");
        }
    }

    /**
     * Sets the current user based on username.
     * Note that the provided String has already been validated by the LoginController.
     * As such, there is no need to check for null pointers, or validate the String here.
     *
     * @param username is the String representation of the username of the current user.
     */
    public void setUser(String username) {
        User user = null;
        try {
            user = getUserStorage().readByUsername(username);
        } catch (IOException e) {
            feedback.setText("An error occurred when loading the current user.");
            return;
        }

        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

package ui.controllers;

import core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import json.CognitionStorage;

import java.io.IOException;

import static core.tools.Tools.capitalize;

public class DashboardController extends LoggedInController {

    public DashboardController(User user, CognitionStorage cognitionStorage) {
        super(user, cognitionStorage);
    }

    @FXML
    private ListView<String> flashcardPane;

    @FXML
    private Label feedback;

    @FXML
    public Labeled heading;

    @FXML
    protected void initialize() {
        heading.setText("Welcome, " + capitalize(getUser().getUsername()));
    }

    @FXML
    public void handleLogout(ActionEvent event) {

        changeToView(event, new LoginController(getCognitionStorage()), "Login", feedback);

    }

    @FXML
    public void handleCreateQuiz(ActionEvent event) {

        changeToView(event, new QuizController(getUser(), getCognitionStorage()), "Quiz", feedback);

    }

    @FXML
    public void handleMyQuizzes(ActionEvent event) {

        changeToView(event, new MyQuizzesController(getUser(), getCognitionStorage()), "MyQuizzes", feedback);

    }

}

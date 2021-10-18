package ui.controllers;

import core.Quiz;
import core.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import json.CognitionStorage;
import ui.controllers.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.util.stream.Collectors;

public class MyQuizzesController extends LoggedInController {

    @SuppressFBWarnings
    public Label feedback;

    @SuppressFBWarnings
    public ListView<String> quizzesListView;
    private Quiz selectedQuiz;
    private String feedbackErrorMessage;

    public MyQuizzesController(User user, CognitionStorage cognitionStorage) {
        super(user, cognitionStorage);
    }

    @FXML
    public void initialize() {
        ObservableList<String> displayedQuizzes = FXCollections.observableArrayList(
                getUser().getQuizzes().stream().map(Quiz::getName).collect(Collectors.toList())
        );

        quizzesListView.setItems(displayedQuizzes);


        quizzesListView.setOnMouseClicked(event -> {
            int index = quizzesListView.getSelectionModel().getSelectedIndex();
            // Prevents IndexOutOfBoundsException if invalid element is selected
            if (index != -1)
                selectedQuiz = getUser().getQuizzes().get(index);
        });


    }

    @FXML
    public void handleStartQuiz(ActionEvent event) {

        if (quizIsNotSelected()) return;

        // Set state in controller
        ViewQuizController viewQuizController = new ViewQuizController(getUser(), selectedQuiz, getCognitionStorage());
        viewQuizController.setQuiz(selectedQuiz);

        changeToView(event, viewQuizController, "ViewQuiz", feedback);

    }

    @FXML
    public void handleUpdateQuiz(ActionEvent event) throws IOException {

        if (quizIsNotSelected()) return;

        // Set state in controller
        QuizController quizController = new QuizController(getUser(), getCognitionStorage());
        quizController.setQuiz(selectedQuiz);

        changeToView(event, quizController, "Quiz", feedback);

    }

    private boolean quizIsNotSelected() {
        if (this.selectedQuiz == null) {
            feedbackErrorMessage = "No selected quiz";
            feedback.setText(feedbackErrorMessage);
            return true;
        }
        return false;
    }

    @FXML
    public void handleDeleteQuiz(ActionEvent actionEvent) {

        if (quizIsNotSelected()) return;

        quizzesListView.setOnMouseClicked(event -> {
            int index = quizzesListView.getSelectionModel().getSelectedIndex();

            // Prevents IndexOutOfBoundsException
            if (index != -1) {

                getUser().getQuizzes().remove(index);

                try {
                    getCognitionStorage().update(getUser().getUUID(), getUser());

                } catch (IOException e) {
                    feedback.setText("An error occurred when trying to delete selected quiz.");
                }

                initialize();

            }


        });

    }


    @FXML
    public void handleCreateQuiz(ActionEvent event) {

        changeToView(event, new QuizController(getUser(), getCognitionStorage()), "Quiz", feedback);


    }

    @FXML
    public void handleLogout(ActionEvent event){

        changeToView(event, new LoginController(getCognitionStorage()), "Login", feedback);

    }


    @FXML
    public void handleDashboard(ActionEvent event) {

        changeToView(event, new DashboardController(getUser(), getCognitionStorage()), "Dashboard", feedback);

    }
}



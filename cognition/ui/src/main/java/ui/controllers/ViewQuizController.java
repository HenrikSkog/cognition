package ui.controllers;

import core.Flashcard;
import core.Quiz;
import core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import json.CognitionStorage;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


public class ViewQuizController extends LoggedInController {

    private Quiz quiz;
    private List<Flashcard> flashcards;
    private int flashcardIndex = 0;
    private int correctAnswerCount = 0;

    @FXML
    private Labeled feedback;

    @FXML
    private TextField answerInput;

    @FXML
    private Text heading;

    @FXML
    private Text flashcardText;

    @FXML
    private Text description;

    public ViewQuizController(User user, Quiz quiz, CognitionStorage cognitionStorage) {
        super(user, cognitionStorage);
        this.quiz = quiz;
    }


    @FXML
    protected void initialize() {
        flashcards = this.quiz.getFlashcards();

        heading.setText(quiz.getName());
        description.setText(quiz.getDescription());

        // Initialize the first flashcard
        flashcardText.setText(flashcards.get(flashcardIndex).getFront());

    }

    public void nextFlashcard() {

        if (flashcardIndex == flashcards.size() - 1) {
            flashcardText.setText("End of quiz! " + "You got " + correctAnswerCount + " right out of " + flashcards.size() + " possible.");
            return;
        }

        flashcardIndex++;
        flashcardText.setText(flashcards.get(flashcardIndex).getFront());
    }


    @FXML
    public void checkAnswer() {
        String userAnswer = answerInput.getText().toLowerCase();
        String correctAnswer = flashcards.get(flashcardIndex).getAnswer().toLowerCase();

        if (answerInput.getText().equals("")) {
            setFeedbackErrorMode(true);
            feedback.setText("Please provide an answer.");
            return;
        }

        if (userAnswer.equals(correctAnswer)) {
            setFeedbackErrorMode(false);
            feedback.setText("Correct answer!");
            answerInput.setText("");
            correctAnswerCount++;
            nextFlashcard();
        } else {
            setFeedbackErrorMode(true);
            feedback.setText("Incorrect! \n The correct answer was: " + correctAnswer + ".");
        }
    }



    @FXML
    public void handleDashboard(ActionEvent event) {

        changeToView(event, new DashboardController(getUser(), getCognitionStorage()), "Dashboard", feedback);

    }

    @FXML
    public void handleCreateQuiz(ActionEvent event) {

        changeToView(event, new QuizController(getUser(), getCognitionStorage()), "Quiz", feedback);

    }

    private void setFeedbackErrorMode(boolean mode) {
        if (mode) {
            feedback.setStyle("-fx-text-fill: red");
        } else {
            feedback.setStyle("-fx-text-fill: green");
        }
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}

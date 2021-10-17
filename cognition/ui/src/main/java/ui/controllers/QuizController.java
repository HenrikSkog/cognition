package ui.controllers;

import core.Flashcard;
import core.Quiz;
import core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import json.CognitionStorage;
import ui.controllers.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuizController extends LoggedInController {
    public ScrollPane flashcardPane;
    private VBox flashcardPaneContainer = new VBox();
    public Labeled feedback;
    public Button storeQuizButton;
    public Text heading;
    public TextField name;
    public TextField description;
    private final String feedbackSuccessMessage = "Successfully created quiz.";
    private String feedbackErrorMessage = "";
    private Quiz quiz = null;

    public QuizController(User user, CognitionStorage cognitionStorage) {
        super(user, cognitionStorage);
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @FXML
    public void initialize() {

        if (quiz != null) {
            // Set state if quiz is to be updated
            name.setText(quiz.getName());
            description.setText(quiz.getDescription());
            storeQuizButton.setText("Update quiz");
            heading.setText("Update " + quiz.getName());

            for (Flashcard flashcard : quiz.getFlashcards()) {
                createFlashcardNode(flashcard);
            }
        } else {
            createFlashcardNode(null);
        }
    }

    /**
     * Removes a Flashcard from the ScrollPane in the UI.
     */
    @SuppressFBWarnings
    public void removeFlashcardNode(int nodeId) {
        flashcardPaneContainer.getChildren().remove(nodeId);

        // Update view
        updateFlashcardNodes();
    }

    public String getFeedbackErrorMessage() {
        return feedbackErrorMessage;
    }

    public String getFeedbackSuccessMessage() {
        return feedbackSuccessMessage;
    }

    public VBox getFlashcardPaneContainer() {
        return flashcardPaneContainer;
    }

    /**
     * Updates view with new index values for relevant components.
     */
    @SuppressFBWarnings
    private void updateFlashcardNodes() {
        feedbackErrorMessage = "An error occurred when updating the flashcard inputs.";

        for (int i = 0; i < flashcardPaneContainer.getChildren().size(); i++) {
            // This is necessary for updating the onAction event handler
            int index = i;

            // Find "remove flashcard" Button and "flashcard number" Text in nested UI component
            VBox parent = (VBox) flashcardPaneContainer.getChildren().get(i);
            HBox upperChildContainer = (HBox) parent.getChildren().stream()
                    .filter(node -> node.getId() != null && node.getId().equals("upper-child-container"))
                    .findFirst()
                    .orElse(null);

            if (upperChildContainer == null) {
                feedback.setText(feedbackErrorMessage);
                return;
            }

            Button removeFlashcardButton = (Button) upperChildContainer.getChildren().stream()
                    .filter(node -> node.getId() != null && node.getId().equals("remove-flashcard-button"))
                    .findFirst()
                    .orElse(null);

            if (removeFlashcardButton == null) {
                feedback.setText(feedbackErrorMessage);
                return;
            }

            Text flashcardNumberText = (Text) upperChildContainer.getChildren().stream()
                    .filter(node -> node.getId() != null && node.getId().equals("flashcard-number-text"))
                    .findFirst()
                    .orElse(null);

            if (flashcardNumberText == null) {
                feedback.setText(feedbackErrorMessage);
                return;
            }

            // Update state in relevant components
            removeFlashcardButton.setOnAction((event) -> removeFlashcardNode(index));
            flashcardNumberText.setText(String.valueOf(index + 1));
        }
    }

    /**
     * Adds a Flashcard to the quiz.
     *
     * @param actionEvent is the ActionEvent from the UI
     */
    @SuppressFBWarnings
    public void addFlashcardNode(ActionEvent actionEvent) {
        createFlashcardNode(null);

        feedback.setText("Added flashcard.");
    }

    @FXML
    public void handleDashboard(ActionEvent event) {

        changeToView(event, new DashboardController(getUser(), getCognitionStorage()), "Dashboard", feedback);

    }

    @FXML
    public void handleMyQuizzes(ActionEvent event) {

        changeToView(event, new MyQuizzesController(getUser(), getCognitionStorage()), "MyQuizzes", feedback);

    }

    @FXML
    public void handleCreateQuiz(ActionEvent event) {

        changeToView(event, new QuizController(getUser(), getCognitionStorage()), "Quiz", feedback);


    }

    /**
     * Dynamically creates a Flashcard node to be used in the view
     */
    @SuppressFBWarnings
    private void createFlashcardNode(Flashcard flashcard) {
        int nodeCount = flashcardPaneContainer.getChildren().size();

        // Define parent and child containers
        VBox parentContainer = new VBox();
        parentContainer.setId("parent-container");
        HBox upperChildContainer = new HBox();
        HBox lowerChildContainer = new HBox();

        // Populate upper child container
        Text flashcardNumberText = new Text();
        flashcardNumberText.setId("flashcard-number-text");
        flashcardNumberText.setText(String.valueOf(nodeCount + 1));

        // Set "remove flashcard" button properties
        Button removeFlashcardButton = new Button();
        removeFlashcardButton.setText("Remove");
        removeFlashcardButton.setId("remove-flashcard-button");
        removeFlashcardButton.setOnAction((event) -> removeFlashcardNode(nodeCount));

        // Add child elements to upper child container
        upperChildContainer.setId("upper-child-container");
        upperChildContainer.getChildren().addAll(flashcardNumberText, removeFlashcardButton);

        // Populate front container
        HBox frontContainer = new HBox();
        frontContainer.setId("front-container");
        Label frontLabel = new Label();
        frontLabel.setText("Front");
        TextField frontInput = new TextField();
        frontInput.setId("front-input");
        if (flashcard != null) {
            frontInput.setText(flashcard.getFront());
        }
        frontContainer.getChildren().addAll(frontLabel, frontInput);

        // Populate answer container
        HBox answerContainer = new HBox();
        answerContainer.setId("answer-container");
        Label answerLabel = new Label();
        answerLabel.setText("Answer");
        TextField answerInput = new TextField();
        answerInput.setId("answer-input");
        if (flashcard != null) {
            answerInput.setText(flashcard.getAnswer());
        }
        answerContainer.getChildren().addAll(answerLabel, answerInput);

        // Create "add flashcard" button
        Button addFlashcardButton = new Button();
        addFlashcardButton.setText("Add");
        addFlashcardButton.setId("add-flashcard-button");
        addFlashcardButton.setOnAction(this::addFlashcardNode);

        // Add child elements to lower child container
        lowerChildContainer.setId("lower-child-container");
        lowerChildContainer.getChildren().addAll(frontContainer, answerContainer, addFlashcardButton);

        // Add child containers to parent container
        parentContainer.getChildren().addAll(upperChildContainer, lowerChildContainer);

        // Add current parent container to overall ScrollPane container
        flashcardPaneContainer.getChildren().add(parentContainer);

        // Update content of ScrollPane
        flashcardPane.setContent(flashcardPaneContainer);
    }

    @SuppressFBWarnings
    public void handleStoreQuiz(ActionEvent actionEvent) {
        String quizName = name.getText();
        String quizDescription = description.getText();

        if (!Quiz.isValidName(quizName)) {
            feedbackErrorMessage = "The name of a quiz cannot be empty.";
            feedback.setText(feedbackErrorMessage);
            return;
        }

        if (!Quiz.isValidDescription(quizDescription)) {
            feedbackErrorMessage = "The description of a quiz cannot be empty.";
            feedback.setText(feedbackErrorMessage);
            return;
        }

        List<Flashcard> flashcards = getFlashcards();

        // Custom feedback response has already been provided to the user in the getFlashcards() method
        if (flashcards == null) return;


        for (Flashcard flashcard : flashcards) {
            if (!Flashcard.isValidFront(flashcard.getFront()) || !Flashcard.isValidAnswer(flashcard.getAnswer())) {
                feedbackErrorMessage = "Both front and answer in each flashcards cannot be empty.";
                feedback.setText(feedbackErrorMessage);
                return;
            }
        }
        // If there is a quiz, update the existing quiz
        if (quiz != null) {
            quiz.setName(quizName);
            quiz.setDescription(quizDescription);
            quiz.setFlashcards(flashcards);
            getUser().updateQuiz(quiz);
        } else {
            // If quiz == null (does not exist), create a new one and add it
            quiz = new Quiz(UUID.randomUUID().toString(), quizName, quizDescription);
            quiz.addFlashcards(flashcards);
            getUser().addQuiz(quiz);
        }

        try {
            getCognitionStorage().update(getUser().getUUID(), getUser());
            feedback.setText(feedbackSuccessMessage);
        } catch (IOException e) {
            feedbackErrorMessage = "An error occurred when trying to create the quiz.";
            feedback.setText(feedbackErrorMessage);
        }
    }

    private List<Flashcard> getFlashcards() {
        List<Flashcard> flashcards = new ArrayList<>();

        for (int i = 0; i < flashcardPaneContainer.getChildren().size(); i++) {
            VBox parent = (VBox) flashcardPaneContainer.getChildren().get(i);
            HBox lowerChildContainer = (HBox) parent.getChildren().stream()
                    .filter(node -> node.getId() != null && node.getId().equals("lower-child-container"))
                    .findFirst()
                    .orElse(null);

            if (lowerChildContainer == null) {
                feedback.setText("An error occurred when parsing the flashcards.");
                return null;
            }

            HBox frontContainer = (HBox) lowerChildContainer.getChildren().stream()
                    .filter(node -> node.getId() != null && node.getId().equals("front-container"))
                    .findFirst()
                    .orElse(null);

            if (frontContainer == null) {
                feedback.setText("An error occurred when parsing the flashcards.");
                return null;
            }

            TextField frontInput = (TextField) frontContainer.getChildren().stream()
                    .filter(node -> node.getId() != null && node.getId().equals("front-input"))
                    .findFirst()
                    .orElse(null);

            if (frontInput == null) {
                feedback.setText("An error occurred when parsing the flashcards.");
                return null;
            }

            HBox answerContainer = (HBox) lowerChildContainer.getChildren().stream()
                    .filter(node -> node.getId() != null && node.getId().equals("answer-container"))
                    .findFirst()
                    .orElse(null);

            if (answerContainer == null) {
                feedback.setText("An error occurred when parsing the flashcards.");
                return null;
            }

            TextField answerInput = (TextField) answerContainer.getChildren().stream()
                    .filter(node -> node.getId() != null && node.getId().equals("answer-input"))
                    .findFirst()
                    .orElse(null);

            if (answerInput == null) {
                feedback.setText("An error occurred when parsing the flashcards.");
                return null;
            }

            // The actual string content of the front input
            String front = frontInput.getText();
            String answer = answerInput.getText();

            if (!Flashcard.isValidFront(front)) {
                feedback.setText("The front of the flashcard cannot be empty.");
                return null;
            }

            if (!Flashcard.isValidFront(answer)) {
                feedback.setText("The answer of the flashcard cannot be empty.");
                return null;
            }

            Flashcard flashcard = new Flashcard(UUID.randomUUID().toString(), front, answer);
            flashcards.add(flashcard);
        }


        return flashcards;
    }

    @FXML
    public void handleLogout(ActionEvent event){
        // Load FXML view
        FXMLLoader loader = getLoader("Login");

        // Set state in controller
        LoginController loginController = new LoginController(getCognitionStorage());
        loader.setController(loginController);

        // Switch stage
        Stage stage = getStage(event);

        try {
            switchScene(stage, loader.load());
        } catch (IOException e) {
            feedback.setText("An error occurred when trying to log out.");
        }
    }

}



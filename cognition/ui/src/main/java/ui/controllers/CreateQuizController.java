package ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import json.UserStorage;
import ui.controllers.annotations.SuppressFBWarnings;

public class CreateQuizController extends Controller {
    public ScrollPane flashcardPane;
    private final VBox flashcardPaneContainer = new VBox();
    public Text feedback;

    @FXML
    public void initialize() {
        createFlashcardNode();
    }

    /**
     * Removes a Flashcard from the ScrollPane in the UI.
     */
    @SuppressFBWarnings
    public void removeFlashcardNode(int nodeId) {
        flashcardPaneContainer.getChildren().remove(nodeId);

        // Update view
        updateFlashcardNodes();

        feedback.setText("Removed flashcard.");
    }

    /**
     * Updates view with new index values for relevant components.
     */
    @SuppressFBWarnings
    private void updateFlashcardNodes() {
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
                feedback.setText("An error occurred when updating the flashcard inputs.");
                return;
            }

            Button removeFlashcardButton = (Button) upperChildContainer.getChildren().stream()
                    .filter(node -> node.getId() != null && node.getId().equals("remove-flashcard-button"))
                    .findFirst()
                    .orElse(null);

            if (removeFlashcardButton == null) {
                feedback.setText("An error occurred when updating the flashcard inputs.");
                return;
            }

            Text flashcardNumberText = (Text) upperChildContainer.getChildren().stream()
                    .filter(node -> node.getId() != null && node.getId().equals("flashcard-number-text"))
                    .findFirst()
                    .orElse(null);

            if (flashcardNumberText == null) {
                feedback.setText("An error occurred when updating the flashcard inputs.");
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
        createFlashcardNode();

        feedback.setText("Added flashcard.");
    }

    /**
     * Dynamically creates a Flashcard node to be used in the view
     */
    @SuppressFBWarnings
    private void createFlashcardNode() {
        int nodeCount = flashcardPaneContainer.getChildren().size();

        // Define parent and child containers
        VBox parentContainer = new VBox();
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
        Label frontLabel = new Label();
        frontLabel.setText("Front");
        TextField frontInput = new TextField();
        frontContainer.getChildren().addAll(frontLabel, frontInput);

        // Populate answer container
        HBox answerContainer = new HBox();
        Label answerLabel = new Label();
        answerLabel.setText("Answer");
        TextField answerInput = new TextField();
        answerContainer.getChildren().addAll(answerLabel, answerInput);

        // Create "add flashcard" button
        Button addFlashcardButton = new Button();
        addFlashcardButton.setText("Add");
        addFlashcardButton.setOnAction(this::addFlashcardNode);

        // Add child elements to lower child container
        lowerChildContainer.getChildren().addAll(frontContainer, answerContainer, addFlashcardButton);

        // Add child containers to parent container
        parentContainer.getChildren().addAll(upperChildContainer, lowerChildContainer);

        // Add current parent container to overall ScrollPane container
        flashcardPaneContainer.getChildren().add(parentContainer);

        // Update content of ScrollPane
        flashcardPane.setContent(flashcardPaneContainer);
    }
}

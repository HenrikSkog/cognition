package ui;

import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import json.CognitionStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextMatchers;
import ui.controllers.QuizController;
import ui.controllers.annotations.SuppressFBWarnings;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import static core.tools.Tools.capitalize;
import static org.junit.jupiter.api.Assertions.fail;

public class QuizTest extends ApplicationTest {
    private Scene scene;
    private QuizController quizController;
    private CognitionStorage cognitionStorage;
    private final String validUsername = "valid-username";
    private final String validPassword = "valid-password";

    @AfterEach
    void tearDown() {
        clearUserStorage();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = getLoader("QuizTest");

        CognitionStorage cognitionStorage = new CognitionStorage("usersTest.json");
        this.cognitionStorage = cognitionStorage;

        // in the app there is no logical way for Create Quiz to be accessed without a logged in user. Thus, we create a fake user here to emulate it
        User loggedInUser = new User(UUID.randomUUID().toString(), validUsername, validPassword);

        cognitionStorage.create(loggedInUser);

        quizController = new QuizController(loggedInUser, cognitionStorage);
        loader.setController(quizController);

        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    @SuppressFBWarnings
    private FXMLLoader getLoader(String fxml) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/" + fxml + ".fxml"));
        return loader;
    }

    @Test
    @DisplayName("Controller is defined.")
    void controllerIsDefined() {
        Assertions.assertNotNull(quizController);
    }

    @Test
    @DisplayName("Storage is defined.")
    void storageIsDefined() {
        Assertions.assertNotNull(cognitionStorage);
    }

    @Test
    @DisplayName("Initial render displays flashcard nodes.")
    void initialRenderDisplaysFlashcardNodes() {
        FxAssert.verifyThat("#flashcard-number-text", TextMatchers.hasText("1"));
    }

    @Test
    @DisplayName("Can remove flashcard.")
    void canRemoveFlashcard() {
        int initialNodeCount = quizController.getFlashcardPaneContainer().getChildren().size();

        clickOn("#remove-flashcard-button");

        int currentNodeCount = quizController.getFlashcardPaneContainer().getChildren().size();

        boolean nodeCountDecreasedOnButtonClick = (currentNodeCount == initialNodeCount - 1);

        Assertions.assertTrue(nodeCountDecreasedOnButtonClick);
    }

    @Test
    @DisplayName("Can add flashcard.")
    void canAddFlashcard() {
        int initialNodeCount = quizController.getFlashcardPaneContainer().getChildren().size();

        clickOn("#add-flashcard-button");

        int currentNodeCount = quizController.getFlashcardPaneContainer().getChildren().size();

        // Denne er true når jeg kjører testen alene, men ikke med mvn clean install.
        boolean nodeCountIncreasedOnButtonClick = (currentNodeCount == initialNodeCount + 1);

        Assertions.assertTrue(nodeCountIncreasedOnButtonClick);
    }

    @Test
    @DisplayName("Can switch to Dashboard.")
    void canSwitchToDashboard() {
        clickOn("#goToDashboardButton");

        // Check that we loaded Dashboard view
        FxAssert.verifyThat("#heading", LabeledMatchers.hasText("Welcome, " + capitalize(validUsername)));
    }

    @Test
    @DisplayName("Invalid input gives correct feedback.")
    void invalidInputGivesCorrectFeedback() {
        // Invalid input
        clickOn("#front-input").write("front");
        clickOn("#answer-input").write("answer");
        verifyInputData("", "description", true);

        clearInputData("#name");
        clearInputData("#description");

        // Description can be empty
        verifyInputData("name", "", false);
    }

    private void clearInputData(String nodeId) {
        doubleClickOn(nodeId).push(KeyCode.BACK_SPACE);
    }

    /**
     * A helper method for verifying input data.
     *
     * @param name           is the provided name in the input form
     * @param description    is the provided description in the input form
     * @param isErrorMessage determines if the feedback is an error message or not.
     */
    private void verifyInputData(String name, String description, boolean isErrorMessage) {
        // Input data into UI
        clickOn("#name").write(name);
        clickOn("#description").write(description);
        clickOn("#storeQuizButton");


        String feedback = isErrorMessage
                ? quizController.getFeedbackErrorMessage()
                : quizController.getFeedbackSuccessMessage();

        // Validate that user got correct feedback in UI
        FxAssert.verifyThat("#feedback", LabeledMatchers.hasText(feedback));
    }

    /**
     * Empties the JSON data in file at the storage path.
     * Used before validating the return type when user storage is empty.
     */
    private void clearUserStorage() {
        try (FileWriter writer = new FileWriter(cognitionStorage.getStoragePath())) {
            writer.write("");
        } catch (IOException e) {
            fail();
        }
    }
}
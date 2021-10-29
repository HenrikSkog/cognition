package ui;

import core.Quiz;
import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import json.CognitionStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import ui.controllers.QuizController;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import static core.tools.Tools.createUuid;
import static org.junit.jupiter.api.Assertions.fail;

public class UpdateQuizTest extends ApplicationTest {
  private final String validUsername = "valid-username";
  private final String validPassword = "valid-password";
  private final String validQuizName = "valid-quiz-name";
  private final String validQuizDescription = "valid-quiz-description";
  private Scene scene;
  private QuizController quizController;
  private CognitionStorage cognitionStorage;

  @AfterEach
  void tearDown() {
    clearUserStorage();
  }

  @Override
  public void start(Stage stage) throws Exception {
           FXMLLoader loader = getLoader("Quiz");

    this.cognitionStorage = new CognitionStorage("cognitionTest.json");


    // in the app there is no logical way for Create Quiz to be accessed without a
    // logged in user. Thus, we create a fake user here to emulate it
    User loggedInUser = new User(createUuid(), validUsername, validPassword);
    Quiz quiz = new Quiz(createUuid(), validQuizName, validQuizDescription);
    loggedInUser.addQuiz(quiz);
    cognitionStorage.create(loggedInUser);

        
    quizController = new QuizController(loggedInUser, cognitionStorage);
    quizController.setQuizBeingUpdated(quiz);
    loader.setController(quizController);

    scene = new Scene(loader.load());
    stage.setScene(scene);
    stage.show();
  }

  private FXMLLoader getLoader(String fxml) {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("views/" + fxml + ".fxml"));
    return loader;
  }

  @Test
  @DisplayName("Updating quiz displays correct nodes.")
  void updatingQuizDisplaysCorrectNodes() {
    TextField nameTextField = (TextField) scene.lookup("#name");
    TextField descriptionTextField = (TextField) scene.lookup("#description");

    Assertions.assertEquals(validQuizName, nameTextField.getText());
    Assertions.assertEquals(validQuizDescription, descriptionTextField.getText());
  }

  /**
   * Empties the JSON data in file at the storage path. Used before validating the
   * return type when user storage is empty.
   */
  private void clearUserStorage() {
    try (FileWriter writer = new FileWriter(cognitionStorage.getStoragePath())) {
      writer.write("");
    } catch (IOException e) {
      fail();
    }
  }

}

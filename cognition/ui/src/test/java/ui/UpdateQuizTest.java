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

import java.io.FileWriter;
import java.io.IOException;

import static core.tools.Tools.createUuid;
import static org.junit.jupiter.api.Assertions.fail;
import static ui.TestFxHelper.waitForFxEvents;

public class UpdateQuizTest extends ApplicationTest {
  private final String validUsername = "valid-username";
  private final String validPassword = "valid-password";
  private final String validQuizName = "valid-quiz-name";
  private final String validQuizDescription = "valid-quiz-description";
  private Scene scene;
  private QuizController quizController;
  private RemoteCognitionAccess remoteCognitionAccess;

  @AfterEach
  void tearDown() {
    clearUserStorage();
  }

  @Override
  public void start(Stage stage) throws Exception {
           FXMLLoader loader = getLoader("Quiz");

    this.remoteCognitionAccess = new RemoteCognitionAccess(AppTest.TEST_PORT);


    // in the app there is no logical way for Create Quiz to be accessed without a
    // logged-in user. Thus, we create a fake user here to emulate it
    User loggedInUser = new User(validUsername, validPassword);
    Quiz quiz = new Quiz(createUuid(), validQuizName, validQuizDescription);
    loggedInUser.addQuiz(quiz);
    remoteCognitionAccess.create(loggedInUser);

        
    quizController = new QuizController(loggedInUser, remoteCognitionAccess);
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
    waitForFxEvents();
    TextField nameTextField = (TextField) scene.lookup("#name");
    waitForFxEvents();
    TextField descriptionTextField = (TextField) scene.lookup("#description");

    waitForFxEvents();
    Assertions.assertEquals(validQuizName, nameTextField.getText());
    waitForFxEvents();
    Assertions.assertEquals(validQuizDescription, descriptionTextField.getText());
  }

  /**
   * Empties the JSON data in file at the storage path. Used before validating the
   * return type when user storage is empty.
   */
  private void clearUserStorage() {
    try (FileWriter writer = new FileWriter(String.valueOf(new CognitionStorage("cognitionTest.json").getStoragePath()))) {
      writer.write("");
    } catch (IOException e) {
      fail();
    }
  }

}

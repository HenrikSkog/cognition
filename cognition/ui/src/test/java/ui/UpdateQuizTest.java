package ui;

import core.Quiz;
import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import static core.tools.Tools.createUuid;

public class UpdateQuizTest extends ApplicationTest {
  private final String validUsername = "valid-username";
  private final String validPassword = "valid-password";
  private final String validQuizName = "valid-quiz-name";
  private final String validQuizDescription = "valid-quiz-description";
  private Scene scene;
  private QuizController quizController;
  private final RemoteCognitionAccess mockRemoteCognitionAccess = Mockito.mock(RemoteCognitionAccess.class);

  private TestFxHelper helper = new TestFxHelper();

  @AfterEach
  void tearDown() {
    TestFxHelper.clearTestStorage();
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = getLoader("Quiz");

    // in the app there is no logical way for Create Quiz to be accessed without a
    // logged-in user. Thus, we create a fake user here to emulate it
    User loggedInUser = new User(validUsername, validPassword);
    Quiz quiz = new Quiz(createUuid(), validQuizName, validQuizDescription);
    loggedInUser.addQuiz(quiz);


    quizController = new QuizController(loggedInUser, mockRemoteCognitionAccess);
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
    TextField nameTextField = (TextField) helper.findTextField(node -> true, "#name", 0);
    TextField descriptionTextField = (TextField) helper.findTextField(node -> true, "#description", 0);

    Assertions.assertEquals(validQuizName, nameTextField.getText());
    Assertions.assertEquals(validQuizDescription, descriptionTextField.getText());
  }

  @Test
  @DisplayName("Cannot create empty quiz.")
  void cannotCreateEmpty() {
    TestFxHelper.waitForFxEvents();
    clickOn("#storeQuizButton");
    TestFxHelper.waitForFxEvents();

    FxAssert.verifyThat("#feedback", LabeledMatchers.hasText("There has to be at least one flashcard"));
  }
}

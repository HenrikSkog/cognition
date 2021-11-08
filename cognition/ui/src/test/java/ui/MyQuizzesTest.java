package ui;

import core.Flashcard;
import core.Quiz;
import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import json.CognitionStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import ui.controllers.MyQuizzesController;
import java.io.FileWriter;
import java.io.IOException;

import static core.tools.Tools.createUuid;
import static org.junit.jupiter.api.Assertions.fail;
import static ui.TestFxHelper.waitForFxEvents;

public class MyQuizzesTest extends ApplicationTest {
  private Scene scene;
  private MyQuizzesController myQuizzesController;
  private RemoteCognitionAccess remoteCognitionAccess;
  private User loggedInUser;

  private ListView<Quiz> listView;

  private final String validUsername = "valid-username";
  private final String validPassword = "valid-password";


  @AfterEach
  void tearDown() {
    clearStorage();
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = getLoader("MyQuizzes");

    // logged in user and storage instances must be present for this view to function
    this.remoteCognitionAccess = new RemoteCognitionAccess(AppTest.TEST_PORT);
    this.loggedInUser = new User(validUsername, validPassword);

    // create some test data. 10 quizzes
    for (int i = 0; i < 10; i++) {
      Quiz quiz = new Quiz(createUuid(), "Test quiz "
              + i, "Test description " + i);
      for (int j = 0; j < 10; j++) {
        Flashcard fc = new Flashcard(createUuid(), "Front"
                + j, "Back" + j);
        quiz.addFlashcard(fc);
      }
      loggedInUser.addQuiz(quiz);
    }

    remoteCognitionAccess.create(loggedInUser);

    myQuizzesController = new MyQuizzesController(loggedInUser, remoteCognitionAccess);

    loader.setController(myQuizzesController);

    scene = new Scene(loader.load());
    stage.setScene(scene);
    stage.show();

    this.listView = myQuizzesController.getListView();
  }

  private FXMLLoader getLoader(String fxml) {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("views/" + fxml + ".fxml"));
    return loader;
  }

  @Test
  @DisplayName("Controller is defined.")
  void controllerIsDefined() {
    Assertions.assertNotNull(myQuizzesController);
  }

  @Test
  @DisplayName("Can display quizzes")
  public void canDisplayQuizzes() {
    if (listView.getItems().size() == 0) {
      fail("No items in list");
    }

    waitForFxEvents();

    // assert listview has all quizzes that user object has
    for (int i = 0; i < listView.getItems().size(); i++) {
      Assertions.assertEquals(
              loggedInUser.getQuizzes().get(i).getUuid(),
              listView.getItems().get(i).getUuid()
      );
    }
  }

  @Test
  @DisplayName("Can delete quiz")
  void canDeleteQuiz() {
    //try to delete quiz without selecting one, should not work and give feedback to user
    clickOn("#deleteQuizButton");
    waitForFxEvents();

    // Validate that user got correct feedback in UI
    Assertions.assertEquals(
            "No selected quiz",
            myQuizzesController.getFeedbackErrorMessage()
    );

    // click on item in list -> click on delete button
    clickOn("#quizzesListView");
    waitForFxEvents();

    // delete selected quiz
    clickOn("#deleteQuizButton");
    waitForFxEvents();

    //  -> make sure number of items are 10 - 1 = 9
    Assertions.assertEquals(9, listView.getItems().size());
    waitForFxEvents();
  }

  @Test
  @DisplayName("Can start quiz")
  public void canStartQuiz() {
    // click on start quiz without selecting quiz first
    clickOn("#startQuizButton");
    waitForFxEvents();

    // cannot start quiz without selecting one first
    Assertions.assertEquals(
            "No selected quiz",
            myQuizzesController.getFeedbackErrorMessage()
    );
    waitForFxEvents();

    // select a quiz
    clickOn("#quizzesListView");
    waitForFxEvents();

    // start quiz
    clickOn("#startQuizButton");
    waitForFxEvents();

    // make sure right view is loaded
    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("ViewQuiz"));
    waitForFxEvents();
  }

  @Test
  @DisplayName("Can update quiz")
  public void canUpdateQuiz() {
    // click on update wihout selecting quiz
    clickOn("#updateQuizButton");
    waitForFxEvents();

    // cannot update quiz without selecting one first
    Assertions.assertEquals(
            "No selected quiz",
            myQuizzesController.getFeedbackErrorMessage()
    );
    waitForFxEvents();

    // click on a quiz
    clickOn("#quizzesListView");
    waitForFxEvents();

    // try to update quiz
    clickOn("#updateQuizButton");
    waitForFxEvents();

    // assert that right view is loaded
    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Quiz"));
    waitForFxEvents();
  }

  @Test
  @DisplayName("Can navigate to Dashboard")
  void canNavigateToDashboard() {
    clickOn("#goToDashboardButton");
    waitForFxEvents();

    FxAssert.verifyThat("#signOutButton", LabeledMatchers.hasText("Sign out"));
    waitForFxEvents();
  }

  @Test
  @DisplayName("Can navigate to Create Quiz")
  void canNavigateToCreateQuiz() {
    clickOn("#createQuizButton");
    waitForFxEvents();

    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Quiz"));
    waitForFxEvents();
  }

  @Test
  @DisplayName("Can log out")
  void canLogOut() {
    clickOn("#signOutButton");
    waitForFxEvents();

    FxAssert.verifyThat("#pageId", LabeledMatchers.hasText("Login"));
    waitForFxEvents();
  }

  private void clearStorage() {
    try (FileWriter writer = new FileWriter(String.valueOf(
            new CognitionStorage("cognitionTest.json").getStoragePath())
    )) {
      writer.write("");
    } catch (IOException e) {
      fail();
    }
  }
}
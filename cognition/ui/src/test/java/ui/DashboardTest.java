package ui;

import core.User;
import core.tools.Tools;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
import ui.controllers.DashboardController;
import ui.controllers.annotations.SuppressFBWarnings;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

public class DashboardTest extends ApplicationTest {
    private Scene scene;
    private DashboardController dashboardController;
    private CognitionStorage cognitionStorage;
    private final String validUsername = "valid-username";
    private final String validPassword = "valid-password";

    @AfterEach
    void tearDown() {
        clearUserStorage();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = getLoader("DashboardTest");

        this.cognitionStorage = new CognitionStorage("usersTest.json");

        // in the app there is no logical way for Create Quiz to be accessed without a logged in user. Thus, we create a fake user here to emulate it
        User loggedInUser = new User(UUID.randomUUID().toString(), validUsername, validPassword);

        cognitionStorage.create(loggedInUser);
        // in the app there is no logical way for Create Quiz to be accessed without a logged in user. Thus, we create a fake user here to emulate it
        this.dashboardController = new DashboardController(loggedInUser, cognitionStorage);

        loader.setController(dashboardController);

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
        Assertions.assertNotNull(dashboardController);
    }

    @Test
    @DisplayName("Storage is defined.")
    void storageIsDefined() {
        Assertions.assertNotNull(cognitionStorage);
    }

    @Test
    @DisplayName("User can log out")
    void userCanLogOut() {
        FxAssert.verifyThat("#heading", LabeledMatchers.hasText("Welcome, " + Tools.capitalize(validUsername)));

        clickOn("#signOutButton");

        FxAssert.verifyThat("#loginButton", LabeledMatchers.hasText("Sign in"));
    }

    @Test
    @DisplayName("Can switch to Create Quiz.")
    void canSwitchToCreateQuiz() {
        FxAssert.verifyThat("#dashboardLogo", TextMatchers.hasText("Cognition"));

        clickOn("#createQuizButton");

        FxAssert.verifyThat("#heading", TextMatchers.hasText("Create new quiz"));
    }

    @Test
    @DisplayName("Can switch to My Quizzes.")
    void canSwitchToMyQuizzes() {
        FxAssert.verifyThat("#dashboardLogo", TextMatchers.hasText("Cognition"));

        clickOn("#switchToMyQuizzesButton");

        FxAssert.verifyThat("#myQuizzesLabel", LabeledMatchers.hasText("My Quizzes"));
    }

    @Test
    @DisplayName("Can set currently active user.")
    void canSetCurrentlyActiveUser() {
        String username = "active-username";
        User user = new User(UUID.randomUUID().toString(), username, "password");

        // Create sample user
        try {
            cognitionStorage.create(user);
        } catch (IOException e) {
            fail();
        }

        // Set active user
        try {
            dashboardController.setUser(user);
        } catch (NullPointerException e) {
            fail();
        }

        // Check that the currently active user is the one we just set as active user
        Assertions.assertEquals(user, dashboardController.getUser());
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

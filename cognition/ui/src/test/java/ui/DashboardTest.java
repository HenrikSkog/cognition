package ui;

import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import json.UserStorage;
import org.junit.jupiter.api.*;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextMatchers;
import ui.controllers.DashboardController;
import ui.controllers.LoginController;
import ui.controllers.annotations.SuppressFBWarnings;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

public class DashboardTest extends ApplicationTest {
    private Scene scene;
    private LoginController loginController;
    private DashboardController dashboardController;
    private UserStorage userStorage;

    @BeforeEach
    void setUp() {
        try {
            userStorage = new UserStorage("usersTest.json");
            loginController.setUserStorage(userStorage);

            // Add a user, such that storage is not empty. Empty storage is tested in core module.
            userStorage.create(
                    new User(UUID.randomUUID().toString(), "placeholder", "placeholder")
            );
        } catch (IOException e) {
            fail();
        }
    }

    @AfterEach
    void tearDown() {
        clearUserStorage();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loginLoader = getLoader("LoginTest");
        loginLoader.load();


        loginController = loginLoader.getController();

        FXMLLoader loader = getLoader("DashboardTest");
        Parent root = loader.load();

        dashboardController = loader.getController();

        scene = new Scene(root);
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
    void controllersAreDefined() {
        Assertions.assertNotNull(loginController);
        Assertions.assertNotNull(dashboardController);
    }

    @Test
    @DisplayName("Storage is defined.")
    void storageIsDefined() {
        Assertions.assertNotNull(userStorage);
    }

    @Test
    @DisplayName("User can log out")
    void userCanLogOut() {

        FxAssert.verifyThat("#heading", LabeledMatchers.hasText("Welcome, username"));

        clickOn("#signOutButton");
        FxAssert.verifyThat("#loginButton", LabeledMatchers.hasText("Sign in"));

    }


    @Test
    @DisplayName("User can open Create Quiz Screen")
    void userCanOpenCreateQuizScreen() {
        clickOn("#createQuizButton");

        FxAssert.verifyThat("#cqHeading", TextMatchers.hasText("Create quiz"));
    }



    /**
     * Empties the JSON data in file at the storage path.
     * Used before validating the return type when user storage is empty.
     */
    private void clearUserStorage() {
        try (FileWriter writer = new FileWriter(userStorage.getStoragePath())) {
            writer.write("");
        } catch (IOException e) {
            fail();
        }
    }
}

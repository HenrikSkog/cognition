package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import json.UserStorage;
import ui.controllers.LoginController;
import ui.controllers.annotations.SuppressFBWarnings;

import java.util.Objects;

public class App extends Application {

    private Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        // Load FXML view
        FXMLLoader loader = getLoader("Login");

        Parent root = loader.load();

        // Set state in controller
        LoginController loginController = loader.getController();
        // The UserStorage that gets initialized here, is common for the entire application.
        // This UserStorage is set in all other controllers, in order to maintain continuity.
        // This logic also, most importantly,
        // separates persistent storage between application logic and unit and end-to-end tests.
        loginController.setUserStorage(new UserStorage());

        // Switch stage
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @SuppressFBWarnings
    public FXMLLoader getLoader(String fxml) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/" + fxml + ".fxml"));
        return loader;
    }

    public void setStyles(String css) {
        scene.getStylesheets().add(Objects.requireNonNull(App.class.getResource("styles/" + css + ".css")).toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}

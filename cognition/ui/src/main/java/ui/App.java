package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import json.CognitionStorage;
import ui.controllers.LoginController;
import ui.controllers.annotations.SuppressFBWarnings;

public class App extends Application {

    private Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        // Load FXML view
        FXMLLoader loader = getLoader("Login");

        // Set state in controller
        // The UserStorage that gets initialized here, is common for the entire application.
        // This UserStorage is set in all other controllers, in order to maintain continuity.
        // This logic also, most importantly,
        // separates persistent storage between application logic and unit and end-to-end tests.
        CognitionStorage cognitionStorage = new CognitionStorage();
        loader.setController(new LoginController(cognitionStorage));

        Parent root = loader.load();

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

    public static void main(String[] args) {
        launch(args);
    }
}

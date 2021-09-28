package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {

    private static Scene scene;

    // Synchronized prevents errors when using static
    // as it only allows a single thread to run at any given time
    // in a multi-threaded environment

    public synchronized void start(Stage stage) throws Exception {
        scene = new Scene(loadFXML("UI"));
        // App.setStyles("main");
        stage.setScene(scene);
        stage.show();

    }

    public void setStyles(String css) {
        scene.getStylesheets().add(Objects.requireNonNull(App.class.getResource("styles/" + css + ".css")).toString());
    }


    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = setFXMLLoader(fxml);
        return fxmlLoader.load();
    }

    public static FXMLLoader setFXMLLoader(String fxml) {
        return new FXMLLoader(App.class.getResource("views/" + fxml + ".fxml"));
    }


    public static void main(String[] args) {
        launch(args);
    }
}

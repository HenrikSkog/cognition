package ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import json.UserStorage;
import ui.controllers.annotations.SuppressFBWarnings;

import java.io.IOException;

public abstract class Controller {
    private UserStorage userStorage;

    public Stage getStage(ActionEvent event) {
        Node node = (Node) event.getSource();
        return (Stage) node.getScene().getWindow();
    }

    @SuppressFBWarnings
    public FXMLLoader getLoader(String fxml) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../views/" + fxml + ".fxml"));
        return loader;
    }

    /**
     * Switches scene in the stage.
     *
     * @param stage is the stage to show
     * @param root  is the root of the loaded FXML
     * @throws IOException if an error occurs when switching scenes.
     */
    public void switchScene(Stage stage, Parent root) throws IOException {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public void setUserStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }
}

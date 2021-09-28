package controllers;

import core.CognitionModel;
import core.Flashcard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import json.CognitionPersistence;
import ui.App;

import java.io.IOException;

public class DashboardController {

    @FXML
    private ListView<String> flashcardPane;



    public void handleLogout() throws IOException {
        App.setRoot("UI");
    }

    @FXML
    public void initialize() {

        CognitionModel model = new CognitionModel();

        model.addFlashcard(new Flashcard(1, "a", "b"));
        model.addFlashcard(new Flashcard(2, "c", "d"));

        // Save the model
        CognitionPersistence persistence = new CognitionPersistence("flashcards.json");
        persistence.setStoragePath("flashcards.json");

        try {
            persistence.saveCognitionModel(model);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            model = persistence.loadCognitionModel();

            ObservableList<String> items = FXCollections.observableArrayList();

            for (Flashcard flashcard : model) {

                items.add(String.valueOf(flashcard));

            }

            flashcardPane.setItems(items);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

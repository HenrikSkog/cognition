package controllers;

import core.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import ui.App;

import java.io.IOException;

public class LoginController extends App {

    @FXML
    private Labeled feedback;
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;

    String inset_username = "admin";
    String inset_password = "password";

    public void handleLogin() {

        String username = usernameInput.getText().toLowerCase();
        String password = passwordInput.getText();

        if (isValidLogin(username, password)) {
            try {
                App.setRoot("Dashboard");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            feedback.setAlignment(Pos.CENTER);
            feedback.setText("Could not find a valid user with that user-pass combo");
        }

    }

    private boolean isValidLogin(String username, String password) {
        return username.equals(inset_username) && password.equals(inset_password);
    }

    @FXML
    public void initialize() {}

}

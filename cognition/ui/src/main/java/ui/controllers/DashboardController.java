package ui.controllers;

import core.User;
import core.tools.Tools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.text.Text;
import json.CognitionStorage;

/**
 * DashboardController has the presentation logic for the DashBoard view.
 */
public class DashboardController extends LoggedInController {

  @FXML
  private Labeled heading;

  @FXML
  private Text cognitionDescription;

  public DashboardController(User user, CognitionStorage cognitionStorage) {
    super(user, cognitionStorage);
  }

  @FXML
  public void initialize() {
    heading.setText("Welcome, " + Tools.capitalize(getUser().getUsername()));
    cognitionDescription.setText("TODO: Add some descriptive text here.");
  }
}

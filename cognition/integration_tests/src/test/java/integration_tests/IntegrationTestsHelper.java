package integration_tests;

import javafx.fxml.FXMLLoader;
import json.CognitionStorage;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * IntegrationTestsHelper is a package-private class,
 * as it is only used in the integration_tests package.
 * It serves as a helper class with static methods when running tests in the integration_tests module.
 */
class IntegrationTestsHelper {
  /**
   * Empties the JSON data in file at the storage path. Used before validating the
   * return type when user storage is empty.
   */
  protected void clearTestStorage() {
    try (FileWriter writer = new FileWriter(String.valueOf(new CognitionStorage("cognitionTest.json").getStoragePath()))) {
      writer.write("");
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Loads an FXML view from the ui module.
   * This is used in order to not duplicate the views across Maven modules.
   *
   * @param fxml is the name of the FXML view.
   * @return an FXMLLoader for the given FXML view.
   */
  protected FXMLLoader loadFromUserInterface(String fxml) {
    FXMLLoader loader = new FXMLLoader();

    // Find the FXML file relative to the App class in the ui module
    URL url = ui.App.class.getResource("views/" + fxml + ".fxml");

    loader.setLocation(url);

    return loader;
  }
}

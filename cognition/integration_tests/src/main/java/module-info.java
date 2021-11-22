module integration_tests {
  requires cognition.core;
  requires cognition.api;

  requires javafx.base;
  requires javafx.controls;
  requires javafx.fxml;

  requires transitive com.google.gson;
  requires java.net.http;

  requires de.jensd.fx.glyphs.fontawesome;

  opens integration_tests to javafx.graphics, javafx.fxml;
}
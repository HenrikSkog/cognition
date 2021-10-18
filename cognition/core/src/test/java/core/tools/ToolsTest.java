package core.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ToolsTest {
  @Test
  @DisplayName("Test capitalize.")
  void testCapitalize() {
    String lowerCase = "string";
    String expected = "String";

    String actual = Tools.capitalize(lowerCase);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Can initialize.")
  void canInitialize() {
    Tools tools = new Tools();

    Assertions.assertNotNull(tools);

    // If we reach this point, the class can be initialized
  }
}

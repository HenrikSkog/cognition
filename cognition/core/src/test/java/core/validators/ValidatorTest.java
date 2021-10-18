package core.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ValidatorTest {
  @Test
  @DisplayName("Can initialize.")
  void canInitialize() {
    Validator validator = new Validator();

    Assertions.assertNotNull(validator);
    // If we reach this point, the class can be initialized
  }
}

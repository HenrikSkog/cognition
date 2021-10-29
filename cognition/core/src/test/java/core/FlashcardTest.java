package core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Objects;

import static core.tools.Tools.createUuid;

public class FlashcardTest {
  private final String validName = "valid-name";
  private final String validAnswer = "valid-description";
  Flashcard flashcard;

  @BeforeEach
  void setUp() {
    flashcard = new Flashcard(createUuid(), validName, validAnswer);
  }

  @Test
  @DisplayName("Can initialize flashcard.")
  void canInitializeFlashcard() {
    Flashcard emptyFlashcard = new Flashcard();
    Flashcard flashcard = new Flashcard(createUuid(), "Valid front", "Valid answer");

    Assertions.assertNotNull(emptyFlashcard);
    Assertions.assertNotNull(flashcard);
    // If we get here, initializing was successful
  }

  @Test
  @DisplayName("Illegal Flashcard throws.")
  void illegalFlashcardThrows() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> new Flashcard("illegal-UUID", "front", "answer"));

    Assertions.assertThrows(IllegalArgumentException.class,
            () -> new Flashcard(createUuid(), "", "answer"));

    Assertions.assertThrows(IllegalArgumentException.class,
            () -> new Flashcard(createUuid(), "front", ""));
  }

  @Test
  @DisplayName("Can be compared using the overridden equals method")
  void canBeCompared() {
    Flashcard copy = new Flashcard(flashcard.getUuid(), flashcard.getFront(), flashcard.getAnswer());
    Flashcard notEqualFlashcard = new Flashcard(flashcard.getUuid(), "Another front", "Another answer");

    Assertions.assertEquals(flashcard, copy);

    Assertions.assertNotEquals(null, flashcard);

    Assertions.assertNotEquals(flashcard, new Object());

    Assertions.assertNotEquals(flashcard, notEqualFlashcard);
  }

  @Test
  @DisplayName("Working toString method")
  void workingToString() {
    String expectedToString = "Flashcard{" + "UUID=" + flashcard.getUuid() + ", front='" + flashcard.getFront() + '\''
            + ", answer='" + flashcard.getAnswer() + '\'' + '}';

    Assertions.assertEquals(flashcard.toString(), expectedToString);
  }

  @Test
  @DisplayName("Working hash method")
  void workingHashMethod() {
    int expectedHash = Objects.hash(flashcard.getUuid(), flashcard.getFront(), flashcard.getAnswer());

    Assertions.assertEquals(expectedHash, flashcard.hashCode());
  }

  @Test
  @DisplayName("Can set front")
  void canSetFront() {
    flashcard.setFront("Valid front");
    Assertions.assertEquals("Valid front", flashcard.getFront());
  }

  @Test
  @DisplayName("Can set front")
  void canSetAnswer() {
    flashcard.setAnswer("Valid answer");
    Assertions.assertEquals("Valid answer", flashcard.getAnswer());
  }

  @Test
  @DisplayName("Can get UUID.")
  void canGetUUID() {
    String UUID = flashcard.getUuid();

    Assertions.assertEquals(UUID, flashcard.getUuid());
  }
}

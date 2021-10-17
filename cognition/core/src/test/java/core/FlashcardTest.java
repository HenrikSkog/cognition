package core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FlashcardTest {
    Flashcard flashcard;
    private final String validName = "valid-name";
    private final String validAnswer = "valid-description";

    @BeforeEach
    void setUp() {
        flashcard = new Flashcard(UUID.randomUUID().toString(), validName, validAnswer);
    }

    @Test
    @DisplayName("Can initialize flashcard.")
    void canInitializeFlashcard() {
        Flashcard emptyFlashcard = new Flashcard();
        Flashcard flashcard = new Flashcard(UUID.randomUUID().toString(), "Valid front", "Valid answer");

        // If we get here, initializing was successful
    }

    @Test
    @DisplayName("Illegal Flashcard throws.")
    void illegalFlashcardThrows() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Flashcard("illegal-UUID", "front", "answer")
        );

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Flashcard(UUID.randomUUID().toString(), "", "answer")
        );

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Flashcard(UUID.randomUUID().toString(), "front", "")
        );
    }

    @Test
    @DisplayName("Can be compared using the overridden equals method")
    void canBeCompared() {
        Flashcard copy = new Flashcard(flashcard.getUUID(), flashcard.getFront(), flashcard.getAnswer());
        Flashcard notEqualFlashcard = new Flashcard(flashcard.getUUID(), "Another front", "Another answer");

        Assertions.assertEquals(flashcard, copy);

        Assertions.assertNotEquals(null, flashcard);

        Assertions.assertNotEquals(flashcard, new Object());

        Assertions.assertNotEquals(flashcard, notEqualFlashcard);
    }

    @Test
    @DisplayName("Working toString method")
    void workingToString() {
        String expectedToString = "Flashcard{" +
                "UUID=" + flashcard.getUUID() +
                ", front='" + flashcard.getFront() + '\'' +
                ", answer='" + flashcard.getAnswer() + '\'' +
                '}';

        Assertions.assertEquals(flashcard.toString(), expectedToString);
    }

    @Test
    @DisplayName("Working hash method")
    void workingHashMethod() {
        int expectedHash = Objects.hash(flashcard.getUUID(), flashcard.getFront(), flashcard.getAnswer());

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
        String UUID = flashcard.getUUID();

        Assertions.assertEquals(UUID, flashcard.getUUID());
    }
}




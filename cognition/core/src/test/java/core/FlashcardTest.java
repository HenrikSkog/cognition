package core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FlashcardTest {
    @Test
    @DisplayName("Illegal Flashcard Throws")
    void illegalFlashcardThrows() {
        // This should pass without throwing
        Flashcard validFlashcard = new Flashcard(1, "This is valid.", "Valid answer");

        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    Flashcard invalidFlashcard = new Flashcard(2, null, "Valid answer");
                });
    }
}

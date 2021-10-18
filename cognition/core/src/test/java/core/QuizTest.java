package core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuizTest {
  private final String validName = "valid-name";
  private final String validDescription = "valid-description";
  private Quiz quiz;

  @BeforeEach
  void setUp() {
    quiz = new Quiz(UUID.randomUUID().toString(), validName, validDescription);
  }

  @Test
  @DisplayName("Can initialize quiz.")
  void canInitializeQuiz() {
    Quiz emptyQuiz = new Quiz();
    Quiz quiz = new Quiz(UUID.randomUUID().toString(), "name", "description");

    Assertions.assertNotNull(emptyQuiz);
    Assertions.assertNotNull(quiz);
    // If we get here, initializing was successful
  }

  @Test
  @DisplayName("Illegal Quiz throws.")
  void illegalQuizThrows() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> new Quiz("invalid-UUID", "name", "description"));

    Assertions.assertThrows(IllegalArgumentException.class, () -> new Quiz(UUID.randomUUID().toString(), "", ""));

    String invalidDescription = "*".repeat(Quiz.MAX_DESCRIPTION_LENGTH + 1);

    Assertions.assertThrows(IllegalArgumentException.class,
            () -> new Quiz(UUID.randomUUID().toString(), "name", invalidDescription));
  }

  @Test
  @DisplayName("Can add flashcards.")
  void canAddFlashcards() {
    List<Flashcard> flashcards = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      flashcards.add(new Flashcard(UUID.randomUUID().toString(), "front-" + i, "answer-" + i));
    }

    quiz.addFlashcards(flashcards);

    Assertions.assertEquals(flashcards, quiz.getFlashcards());
  }

  @Test
  @DisplayName("Can set flashcards.")
  void canSetFlashcards() {
    quiz.setFlashcards(null);

    boolean didNotAdd = quiz.getFlashcards().size() == 0;

    Assertions.assertTrue(didNotAdd);

    quiz.setFlashcards(List.of(new Flashcard(UUID.randomUUID().toString(), "front-1", "answer-2"),
            new Flashcard(UUID.randomUUID().toString(), "front-2", "answer-2")));

    boolean addedTwoFlashcards = quiz.getFlashcards().size() == 2;

    Assertions.assertTrue(addedTwoFlashcards);
  }

  @Test
  @DisplayName("No duplicate flashcards.")
  void noDuplicateFlashcards() {
    Flashcard flashcard = new Flashcard(UUID.randomUUID().toString(), "front", "answer");

    // Add flashcard for the first time
    quiz.addFlashcard(flashcard);

    // Add flashcard for the second time. This time, it should not be added.
    quiz.addFlashcard(flashcard);

    // Flashcards should only be added once
    Assertions.assertEquals(1, quiz.getFlashcards().size());
  }

  @Test
  @DisplayName("Can add flashcard.")
  void canAddFlashcard() {
    quiz.addFlashcard(null);

    // Flashcard should not have been added
    Assertions.assertEquals(0, quiz.getFlashcards().size());

    quiz.addFlashcard(new Flashcard(UUID.randomUUID().toString(), "front", "answer"));

    // Flashcard should have been added
    Assertions.assertEquals(1, quiz.getFlashcards().size());
  }

  @Test
  @DisplayName("Can remove flashcard.")
  void canRemoveFlashcard() {
    Flashcard nonExistingFlashcard = new Flashcard(UUID.randomUUID().toString(), "non-existing-front",
            "non-existing-asnwer");
    Flashcard existingFlashcard = new Flashcard(UUID.randomUUID().toString(), "existing-front", "existing-asnwer");

    // Add flashcard and verify number of flashcards
    quiz.addFlashcard(existingFlashcard);
    Assertions.assertEquals(1, quiz.getFlashcards().size());

    // Remove non-existing flashcard and verify that size is unchanged
    quiz.removeFlashcard(nonExistingFlashcard);
    Assertions.assertEquals(1, quiz.getFlashcards().size());

    // Remove existing flashcard, and verify that size decrease by 1
    quiz.removeFlashcard(existingFlashcard);
    Assertions.assertEquals(0, quiz.getFlashcards().size());
  }

  @Test
  @DisplayName("Can set name.")
  void canSetName() {
    String currentName = quiz.getName();

    Assertions.assertThrows(IllegalArgumentException.class, () -> quiz.setName(""));

    // Verify that the name is unchanged when provided with an empty string
    Assertions.assertEquals(currentName, quiz.getName());

    String newName = "new-name";
    quiz.setName(newName);

    // Verify that the name is changed when provided with a new name
    Assertions.assertEquals(quiz.getName(), newName);
  }

  @Test
  @DisplayName("Can set description.")
  void canSetDescription() {
    String currentDescription = quiz.getDescription();

    // Verify that the name is unchanged when provided with an empty string
    Assertions.assertEquals(quiz.getDescription(), currentDescription);

    String newDescription = "new-name";
    quiz.setDescription(newDescription);

    // Verify that the name is changed when provided with a new name
    Assertions.assertEquals(quiz.getDescription(), newDescription);
  }
}

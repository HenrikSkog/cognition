package core;


import core.validators.Validator;
import java.util.ArrayList;
import java.util.List;


/**
 * The class Quiz - Creates a new Quiz object that stores a name, a description
 * and a list of flashcards. Is then used to show quizzes in the frontend (UI).
 */
public class Quiz {
  public static final int MAX_DESCRIPTION_LENGTH = 500;
  private String uuid;
  private String name;
  private String description;
  private List<Flashcard> flashcards = new ArrayList<>();

  public Quiz() {
  }

  /**
   * Constructor that takes in all the required parameters of the Quiz and
   * validates them.
   *
   * @param uuid        is the Universal Unique ID of the Quiz
   * @param name        is the name of the Quiz
   * @param description is the description of the Quiz
   */
  public Quiz(String uuid, String name, String description) {
    setUuid(uuid);
    setName(name);
    setDescription(description);
  }

  public static boolean isValidName(String name) {
    return name != null && name.length() >= 1;
  }

  public static boolean isValidDescription(String description) {
    return description != null && description.length() < MAX_DESCRIPTION_LENGTH;
  }

  /**
   * Loops through all provided flashcards and runds them through the singular
   * addFlashcard method.
   *
   * @param flashcards is the provided list of flashcards
   */
  public void addFlashcards(List<Flashcard> flashcards) {
    for (Flashcard flashcard : flashcards) {
      addFlashcard(flashcard);
    }
  }

  public String getUuid() {
    return uuid;
  }

  private void setUuid(String uuid) {
    if (!Validator.isValidUuid(uuid)) {
      throw new IllegalArgumentException();
    }

    this.uuid = uuid;
  }

  /**
   * Adds a flashcard to the list of flashcards, if the list does not already
   * contain it and the flashcard is not null.
   *
   * @param flashcard is a flashcard object
   */
  public void addFlashcard(Flashcard flashcard) {
    if (flashcard == null) {
      return;
    }

    if (!flashcards.contains(flashcard)) {
      flashcards.add(flashcard);
    }
  }

  public void removeFlashcard(Flashcard flashcard) {
    // Removes only if it is present. Thus, not conditional is needed.
    flashcards.remove(flashcard);
  }

  public String getName() {
    return name;
  }

  /**
   * Checks if the name is a valid name, else throw IllegalArgumentException.
   *
   * @param name is the name of the Quiz
   */
  public void setName(String name) {
    if (!Quiz.isValidName(name)) {
      throw new IllegalArgumentException();
    }

    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  /**
   * Checks if the description provided is a valid description If not then throw
   * IllegalArgumentException.
   *
   * @param description is the description of the Quiz
   */
  public void setDescription(String description) {
    if (!Quiz.isValidDescription(description)) {
      throw new IllegalArgumentException();
    }
    this.description = description;
  }

  public List<Flashcard> getFlashcards() {
    return flashcards;
  }


  /**
   * Sets the list of flashcards to the provided flashcards if the list is not
   * null.
   */
  public void setFlashcards(List<Flashcard> flashcards) {
    if (flashcards == null) {
      return;
    }

    this.flashcards = flashcards;
  }
}

package core;

import core.validators.Validator;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    public static final int MAX_DESCRIPTION_LENGTH = 500;
    private String UUID;
    private String name;
    private String description;
    private List<Flashcard> flashcards = new ArrayList<>();

    public Quiz() {
    }

    public Quiz(String UUID, String name, String description) {
        setUUID(UUID);
        setName(name);
        setDescription(description);
    }

    private void setUUID(String UUID) {
        if (!Validator.isValidUUID(UUID)) {
            throw new IllegalArgumentException();
        }

        this.UUID = UUID;
    }

    public void addFlashcards(List<Flashcard> flashcards) {
        for (Flashcard flashcard : flashcards) {
            addFlashcard(flashcard);
        }
    }

    public String getUUID() {
        return UUID;
    }

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

    public void setName(String name) {
        if (!Quiz.isValidName(name)) {
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    public static boolean isValidName(String name) {
        return name != null && name.length() >= 1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (!Quiz.isValidDescription(description)) {
            throw new IllegalArgumentException();
        }
        this.description = description;
    }

    public static boolean isValidDescription(String description) {
        return description != null && description.length() < MAX_DESCRIPTION_LENGTH;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<Flashcard> flashcards) {
        if (flashcards == null) {
            return;
        }

        this.flashcards = flashcards;
    }
}

package core;

import core.validators.Validator;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String UUID;
    private String name;
    private String description;
    private final List<Flashcard> flashcards = new ArrayList<>();

    public Quiz() {
    }

    public Quiz(String UUID, String name, String description) {
        setUUID(UUID);
        setName(name);
        setDescription(description);
    }

    private void setUUID(String UUID) {
        if (Validator.isValidUUID(UUID)) {
            this.UUID = UUID;
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
        if (name.equals("")) {
            return;
        }

        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description.length() < 500) {
            this.description = description;
        }
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }
}

package core;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String id;
    private String name;
    private String description;
    private List<Flashcard> flashcards = new ArrayList<>();

    public Quiz() {
    }

    public Quiz(String id) {
        this.id = id;
    }

    public Quiz(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Quiz(String id, String name, String description, List<Flashcard> flashcards) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.flashcards = flashcards;
    }

    public String getId() {
        return id;
    }

    public void addFlashcard(Flashcard flashcard) {
        flashcards.add(flashcard);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }
}

package core;

import java.util.*;

/**
 * Please note that this model of Flashcards will later be abstracted if need be.
 */
public class CognitionModel implements Iterable<Flashcard> {
    private final List<Flashcard> flashcards = new ArrayList<>();

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    /**
     * Enables iteration through multiple Flashcard instances.
     *
     * @return an Iterator for Flashcard objects.
     */
    @Override
    public Iterator<Flashcard> iterator() {
        return flashcards.iterator();
    }

    /**
     * @param flashcard is an instance of the Flashcard class.
     * @return a boolean representing whether the operation was successful.
     */
    public boolean addFlashcard(Flashcard flashcard) {
        return flashcards.add(flashcard);
    }

    @Override
    public String toString() {
        return "CognitionModel{" +
                "flashcards=" + flashcards +
                '}';
    }
}

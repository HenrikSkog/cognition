package core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Please note that this model of Flashcards will later be abstracted if need be.
 */
public class CognitionModel implements Iterable<Flashcard> {
    // Currently, imagine this HashMap as a JSON representation of data.
    // The key represents the identifier og the JSON object, and the Flashcard is the JSON object
    // NOTE: This is not the final JSON model
    private final Map<Long, Flashcard> map = new HashMap<>();

    public Map<Long, Flashcard> getMap() {
        return map;
    }

    /**
     * Enables iteration through multiple Flashcard instances.
     *
     * @return an Iterator for Flashcard objects.
     */
    @Override
    public Iterator<Flashcard> iterator() {
        return map.values().iterator();
    }

    /**
     * Gets a Flashcard with a given identifier.
     *
     * @param id is the identifier.
     * @return an instance of the Flashcard class.
     */
    public Flashcard getFlashCard(long id) {
        return map.get(id);
    }

    /**
     * @param flashcard is an instance of the Flashcard class.
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    public Flashcard putFlashCard(Flashcard flashcard) {
        return map.put(flashcard.getId(), flashcard);
    }
}

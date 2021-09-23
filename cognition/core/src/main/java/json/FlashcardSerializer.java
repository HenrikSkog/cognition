package json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import core.Flashcard;

import java.io.IOException;

public class FlashcardSerializer extends JsonSerializer<Flashcard> {
    /**
     * Serializes a Flashcard object.
     * A Flashcard is stored on the format:
     * <p>
     * {
     * "id": long,
     * "front": String,
     * "answer": String
     * }
     *
     * @param flashcard is the Flashcard object to serialize.
     * @param generator is the JSON Generator
     * @param provider  is the Serializer Provider
     * @throws IOException if an error occurred when trying to serialize
     */
    @Override
    public void serialize(Flashcard flashcard,
                          JsonGenerator generator,
                          SerializerProvider provider) throws IOException {
        generator.writeStartObject();

        generator.writeNumberField("id", flashcard.getId());
        generator.writeStringField("front", flashcard.getFront());
        generator.writeStringField("answer", flashcard.getAnswer());

        generator.writeEndObject();
    }
}

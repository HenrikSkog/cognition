package json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import core.Flashcard;

import java.io.IOException;

public class FlashcardDeserializer extends JsonDeserializer<Flashcard> {

    /**
     * Deserializes Flashcard objects.
     *
     * @param parser  is the JSON parser.
     * @param context is the deserialization context.
     * @return an instance of the Flashcard class
     * @throws IOException if an error occurred when trying to deserialize the JSON data
     */
    @Override
    public Flashcard deserialize(JsonParser parser,
                                 DeserializationContext context) throws IOException {
        TreeNode treeNode = parser.getCodec().readTree(parser);
        return deserialize((JsonNode) treeNode);
    }

    /**
     * Deserializes a JSON node and converts it to a Flashcard object.
     *
     * @param jsonNode is the JSON node
     * @return an instance of the Flashcard class
     */
    public Flashcard deserialize(JsonNode jsonNode) {
        if (jsonNode instanceof ObjectNode objectNode) {
            Flashcard flashcard = new Flashcard();

            // Set identifier
            JsonNode idNode = objectNode.get("id");
            if (idNode instanceof NumericNode) {
                flashcard.setId(idNode.asLong());
            }

            // Set front
            JsonNode frontNode = objectNode.get("front");
            if (frontNode instanceof TextNode) {
                flashcard.setFront(frontNode.asText());
            }

            // Set answer
            JsonNode answerNode = objectNode.get("answer");
            if (frontNode instanceof TextNode) {
                flashcard.setAnswer(answerNode.asText());
            }

            return flashcard;
        }
        return null;
    }
}

package json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import core.Flashcard;


/**
 * CognitionModule configures serialization and deserialization of CognitionModel objects.
 */
public class CognitionModule extends SimpleModule {
    private static final String NAME = "CognitionModule";

    /**
     * When initializing CognitionModule, all necessary serializers and deserializers are added.
     */
    public CognitionModule() {
        super(NAME, Version.unknownVersion());
        addSerializer(Flashcard.class, new FlashcardSerializer());
    }
}

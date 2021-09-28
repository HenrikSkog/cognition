package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.CognitionModel;
import core.Flashcard;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CognitionPersistence {
    private final ObjectMapper mapper = new ObjectMapper();
    private Path storagePath;

    /**
     * Initializes a new instance of the CognitionPersistence class.
     * Used when we want to store data locally with a given filename.
     *
     * @param filename is the filename. No need to add a full path. Example: flashcards.json
     */
    public CognitionPersistence(String filename) {
        setStoragePath(filename);

        mapper.registerModule(new CognitionModule());
    }


    /**
     * Initializes a new instance of the CognitionPersistence class.
     * Used when we don't necessarily want to store data locally.
     */
    public CognitionPersistence() {
        mapper.registerModule(new CognitionModule());
    }


    /**
     * Reads the CognitionModel.
     *
     * @param reader is an extension of the Reader class.
     * @return the result after mapper has read the model
     * @throws IOException if an error occurred when trying to read the CognitionModel
     */
    public CognitionModel readCognitionModel(Reader reader) throws IOException {
        return mapper.readValue(reader, CognitionModel.class);
    }

    /**
     * Writes the CognitionModel.
     *
     * @param writer is an extension of the Writer class.
     * @throws IOException if an error occurred when trying to read the CognitionModel
     */
    public void writeCognitionModel(Writer writer, CognitionModel model) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(writer, model);
    }

    /**
     * Sets the storage path of the JSON data.
     *
     * @param filename is the filename of the JSON data
     */
    public void setStoragePath(String filename) {
        storagePath = Paths.get(System.getProperty("user.home"), filename);
    }

    /**
     * Loads an instance of the CognitionModel class from the storagePath.
     *
     * @return the loaded model
     * @throws IOException           if an error occurred when reading the model
     * @throws IllegalStateException if no file path for saving data has been set
     */
    public CognitionModel loadCognitionModel() throws IOException, IllegalStateException {
        if (storagePath == null) {
            throw new IllegalStateException("No file path has been set.");
        }

        try (Reader reader = new FileReader(storagePath.toFile(), StandardCharsets.UTF_8)) {
            return readCognitionModel(reader);
        }

    }

    /**
     * Saves an instance of the CognitionModel class to the storagePath
     *
     * @param model is the instance of the CognitionModel class
     * @throws IOException           if an error occurred when saving the model
     * @throws IllegalStateException if no file path for saving data has been set
     */
    public void saveCognitionModel(CognitionModel model) throws IOException, IllegalStateException {
        if (storagePath == null) {
            throw new IllegalStateException("No file path has been set.");
        }

        try (Writer writer = new FileWriter(storagePath.toFile(), StandardCharsets.UTF_8)) {
            writeCognitionModel(writer, model);
        }
    }

    public static void main(String[] args) {
        // NOTE: This is only temporary code to test serialization / deserialization.
        // It will be removed later in the project.
        // For now, it serves to prove that we're able to serialize and deserialize.

        // Add some sample data to the model
        CognitionModel model = new CognitionModel();
        model.addFlashcard(new Flashcard(1, "a", "b"));
        model.addFlashcard(new Flashcard(2, "c", "d"));

        // Save the model
        CognitionPersistence persistence = new CognitionPersistence("flashcards.json");
        persistence.setStoragePath("flashcards.json");
        try {
            persistence.saveCognitionModel(model);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load the model and print the Flashcard objects
        try {
            model = persistence.loadCognitionModel();

            for (Flashcard flashcard : model) {
                System.out.println(flashcard);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

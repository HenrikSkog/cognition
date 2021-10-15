package json;

import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import core.Quiz;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;

public class QuizStorage extends Storage<Quiz> {

    public QuizStorage() throws IOException {
        super("quizzes.json");
    }

    public QuizStorage(String filename) throws IOException {
        super(filename);
    }

    /**
     * Reads all quizzes from storage and deserializes the JSON array to a list of Quiz objects
     *
     * @return a List of Quiz objects
     * @throws IOException if an error occurred when trying to read from the storage file
     */
    public List<Quiz> readQuizzes() throws IOException {
        if (isEmpty()) {
            return null;
        }

        try {
            String content = Files.readString(Path.of(getStoragePath()), StandardCharsets.US_ASCII);
            return getGson().fromJson(
                    content,
                    new TypeToken<List<Quiz>>() {
                    }.getType()
            );
        } catch (IOException e) {
            throw new IOException(getStoragePath() + " is present, but an error occurred when reading all Quizzes from storage.");
        }
    }

    /**
     * Writes a list of Quiz objects to local storage.
     *
     * @param quizzes is a list of Quiz objects.
     * @throws IOException     if an error occurred when trying to write to local storage.
     * @throws JsonIOException if an error occurred when serializing the JSON content.
     */
    private void writeToJson(List<Quiz> quizzes) throws JsonIOException, IOException {
        try (FileWriter writer = new FileWriter(getStoragePath(), StandardCharsets.US_ASCII)) {
            try {
                getGson().toJson(quizzes, writer);
            } catch (JsonIOException e) {
                writer.flush();
                writer.close();
                throw new JsonIOException("An error occurred when serializing the JSON content.");
            }

            writer.flush();
        }
    }

    /**
     * Reads all Quizzes from file, appends the provided Quiz, and then overwrites local storage file.
     * To maintain consistency, one Quiz is written if local storage is empty.
     *
     * @param instance is the Quiz that should be written to file
     */
    @Override
    public void create(Quiz instance) throws IOException {
        List<Quiz> newQuizzes;

        List<Quiz> quizzes = readQuizzes();
        if (quizzes == null) {
            //To maintain consistency in the dataset, a single Quiz is also stored in a list
            newQuizzes = List.of(instance);
        } else {
            quizzes.add(instance);
            newQuizzes = quizzes;
        }

        writeToJson(newQuizzes);
    }

    @Override
    public Quiz read(String identifier) throws NullPointerException, IOException {
        List<Quiz> quizzes = readQuizzes();

        if (quizzes == null) {
            throw new NullPointerException("No quizzes in local storage.");
        }

        // Filters based on id and returns null if no match was found
        return quizzes.stream()
                .filter(quiz -> quiz.getUUID().equals(identifier))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds a Quiz based on an identifier and lets a consumer modify the Quiz list based on it.
     * Writes the new Quiz list to file.
     *
     * @param identifier Quiz that should be taken action upon
     * @param action     consumer that specifies the action
     * @throws IOException            if an error occurred when trying to read the Quiz from local storage
     * @throws NullPointerException   if no Quizzes exist in local storage
     * @throws NoSuchElementException if no Quiz with the given identifier was found.
     */
    private void updateQuiz(String identifier, BiConsumer<List<Quiz>, Integer> action) throws IOException, NullPointerException, NoSuchElementException {
        List<Quiz> quizzes = readQuizzes();

        if (quizzes == null) {
            throw new NullPointerException("No quizzes in local storage.");
        }

        for (int i = 0; i < quizzes.size(); i++) {
            Quiz quiz = quizzes.get(i);
            if (quiz.getUUID().equals(identifier)) {
                // Accept the provided action, passed in as parameter
                action.accept(quizzes, i);

                writeToJson(quizzes);
                return;
            }
        }

        // If loop is finished, no Quiz with the given identifier was found
        throw new NoSuchElementException("No Quiz with identifier \"" + identifier + "\" was found.");
    }

    @Override
    public void update(String identifier, Quiz instance) throws IOException {
        updateQuiz(identifier, (quizzes, index) -> {
            // Remove old Quiz
            quizzes.remove((int) index);

            // Add new Quiz
            quizzes.add(index, instance);
        });
    }

    @Override
    public void delete(String identifier) throws IOException {
        updateQuiz(identifier, ((quizzes, integer) -> {
            // Remove old Quiz
            quizzes.remove((int) integer);
        }));
    }
}

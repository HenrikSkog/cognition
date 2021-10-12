package json;

import com.google.gson.JsonIOException;
import core.Quiz;
import org.junit.jupiter.api.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

public class QuizStorageTest {
    private QuizStorage quizStorage;

    @BeforeEach
    void setUp() {
        try {
            quizStorage = new QuizStorage("quizzesTest.json");
        } catch (IOException e) {
            fail();
        }
    }

    @AfterEach
    void tearDown() {
        clearQuizStorage();
    }

    /**
     * Test passes if a quiz can be created.
     * Test fails if an exception is thrown.
     */
    @Test
    @DisplayName("Can create quiz.")
    void canCreateQuiz() {
        createQuiz(new Quiz(UUID.randomUUID().toString(), "create-name", "create-description"));
    }

    /**
     * Test passes if quizzes can be created.
     * Test fails if an exception is thrown.
     */
    @Test
    @DisplayName("Can create quizzes.")
    void canCreateQuizzes() {
        int NUMBER_OF_QUIZZES = 10;

        for (int i = 0; i < NUMBER_OF_QUIZZES; i++) {
            // Try to create a sample quiz
            try {
                createQuiz(new Quiz(
                        UUID.randomUUID().toString(),
                        "created-name-" + i,
                        "created-description-" + i)
                );
            } catch (JsonIOException e) {
                fail();
            }
        }
    }

    /**
     * An internal method used during testing due to many tests requiring the creation of a test quiz.
     *
     * @param quiz is the quiz to create
     */
    private void createQuiz(Quiz quiz) {

        // Try to create a sample quiz
        try {
            quizStorage.create(quiz);
        } catch (JsonIOException | IOException e) {
            fail();
        }
    }

    /**
     * Test passes if a sample quiz can be read from local storage.
     * Test fails if an exception is thrown.
     */
    @Test
    @DisplayName("Can read quiz.")
    void canReadQuiz() {
        // The identifier is used to determine if the read quiz is the correct one
        String identifier = UUID.randomUUID().toString();

        // Create sample quiz with given identifier
        createQuiz(new Quiz(identifier, "read-name", "read-description"));

        Quiz quiz = new Quiz();

        try {
            quiz = quizStorage.read(identifier);
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Test passes if a sample quiz can be updated based on identifier.
     * Test fails if an exception is thrown.
     */
    @Test
    @DisplayName("Can update quiz.")
    void canUpdateQuiz() {
        // The identifier is used to determine if the updated quiz is the correct one
        String identifier = UUID.randomUUID().toString();

        // Create sample quiz with given identifier
        createQuiz(new Quiz(identifier, "base-name", "base-description"));

        try {
            quizStorage.update(identifier, new Quiz(identifier, "new-name", "new-description"));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Can delete quiz.")
    void canDeleteQuiz() {
        // The identifier is used to determine if the updated quiz is the correct one
        String identifier = UUID.randomUUID().toString();

        // Create sample quiz with given identifier
        createQuiz(new Quiz(identifier, "delete-name", "delete-description"));

        try {
            quizStorage.delete(identifier);
        } catch (IOException e) {
            fail();
        }

        List<Quiz> quizzes = new ArrayList<>();

        try {
            quizzes = quizStorage.readQuizzes();
        } catch (IOException e) {
            fail();
        }

        // Check if Quiz object with given identifier really was deleted
        for (Quiz quiz : quizzes) {
            // There still exists a Quiz object with the randomly generated identifier.
            if (quiz.getId().equals(identifier)) {
                fail();
            }
        }
    }

    @Test
    @DisplayName("No quizzes returns null.")
    void noQuizzesStoredReturnsNull() {
        List<Quiz> quizzes = new ArrayList<>();

        // Clear quiz storage before validating the return type when quiz storage is empty.
        clearQuizStorage();

        try {
            quizzes = quizStorage.readQuizzes();
        } catch (IOException e) {
            fail();
        }

        // Because we have not added any quizzes to the local storage, the returned value of
        // quizStorage.readQuizzes() should be null.
        Assertions.assertNull(quizzes);
    }

    /**
     * Empties the JSON data in file at the storage path.
     * Used before validating the return type when quiz storage is empty.
     */
    private void clearQuizStorage() {
        try (FileWriter writer = new FileWriter(quizStorage.getStoragePath())) {
            writer.write("");
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Has correct storage path.")
    void hasCorrectStoragePath() {
        Assertions.assertEquals(
                String.valueOf(Paths.get(System.getProperty("user.home"), "it1901-gr2103", "cognition", "quizzesTest.json")),
                quizStorage.getStoragePath()
        );
    }
}

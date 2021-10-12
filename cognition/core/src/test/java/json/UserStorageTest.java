package json;

import com.google.gson.JsonIOException;
import core.Flashcard;
import core.Quiz;
import core.User;
import org.junit.jupiter.api.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

public class UserStorageTest {

    private UserStorage userStorage;

    @BeforeEach
    void setUp() {
        try {
            userStorage = new UserStorage("usersTest.json");
        } catch (IOException e) {
            fail();
        }
    }

    @AfterEach
    void tearDown() {
        clearUserStorage();
    }

    /**
     * Test passes if a user can be created.
     * Test fails if an exception is thrown.
     */
    @Test
    @DisplayName("Can create user.")
    void canCreateUser() {
        createUser(new User(UUID.randomUUID().toString(), "created-username", "created-password"));
    }

    /**
     * Test passes if users can be created.
     * Test fails if an exception is thrown.
     */
    @Test
    @DisplayName("Can create users.")
    void canCreateUsers() {
        int NUMBER_OF_USERS = 10;

        for (int i = 0; i < NUMBER_OF_USERS; i++) {
            // Try to create a sample user
            try {
                createUser(new User(
                        UUID.randomUUID().toString(),
                        "created-username-" + i,
                        "created-password-" + i)
                );
            } catch (JsonIOException e) {
                fail();
            }
        }
    }

    @Test
    @DisplayName("Can serialize nested objects.")
    void canSerializeNestedObjects() {
        // expected is used to validate the nested object that gets serialized
        String expected = getCorrectNestedObject();

        // Create sample data that is nested
        int NUMBER_OF_QUIZZES = 1;
        int NUMBER_OF_FLASHCARDS_PER_QUIZ = 2;

        String userId = "user-id";

        User user = new User(userId, "username", "password");

        for (int i = 0; i < NUMBER_OF_QUIZZES; i++) {
            Quiz quiz = new Quiz("quiz-" + i, "quiz-" + i, "description-" + i);

            for (int j = 0; j < NUMBER_OF_FLASHCARDS_PER_QUIZ; j++) {
                Flashcard flashcard = new Flashcard("flashcard-" + j, "front-" + j, "answer-" + j);
                quiz.addFlashcard(flashcard);
            }

            user.addQuiz(quiz);
        }

        // Clear user storage in order to prevent influence from other tests of persistent storage
        clearUserStorage();

        try {
            userStorage.create(user);
        } catch (IOException e) {
            fail();
        }

        // Read content of user storage as pure String
        String actual = "";
        try {
            actual = Files.readString(Path.of(userStorage.getStoragePath()));
        } catch (IOException e) {
            fail();
        }

        Assertions.assertEquals(expected, actual);
    }

    /**
     * A helper method when testing canSerializeNestedObjects.
     * The correct String is encapsulated in a method in order to hide implementation detail and
     * make the developer workflow less cluttered.
     * All values in the JSON data are deterministic.
     *
     * @return a String representation of the correct nested objects, with all whitespace removed.
     */
    private String getCorrectNestedObject() {
        return """
                [
                    {
                        "UUID": "user-id",
                        "username": "username",
                        "password": "password",
                        "quizzes": [
                            {
                                "id": "quiz-0",
                                "name": "quiz-0",
                                "description": "description-0",
                                "flashcards": [
                                    {
                                        "UUID": "flashcard-0",
                                        "front": "front-0",
                                        "answer": "answer-0"
                                    },
                                    {
                                        "UUID": "flashcard-1",
                                        "front": "front-1",
                                        "answer": "answer-1"
                                    }
                                ]
                            }
                        ]
                    }
                ]""".replaceAll("\\s+", "");
    }

    /**
     * An internal method used during testing due to many tests requiring the creation of a test user.
     *
     * @param user is the user to create
     */
    private void createUser(User user) {
        // Try to create a sample user
        try {
            userStorage.create(user);
        } catch (JsonIOException | IOException e) {
            fail();
        }
    }

    /**
     * Test passes if a sample user can be read from local storage.
     * Test fails if an exception is thrown.
     */
    @Test
    @DisplayName("Can read user.")
    void canReadUser() {
        // The identifier is used to determine if the read user is the correct one
        String identifier = UUID.randomUUID().toString();

        // Create sample user with given identifier
        createUser(new User(identifier, "read-username", "read-password"));

        User user = new User();

        try {
            user = userStorage.read(identifier);
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Test passes if a sample user can be updated based on identifier.
     * Test fails if an exception is thrown.
     */
    @Test
    @DisplayName("Can update user.")
    void canUpdateUser() {
        // The identifier is used to determine if the updated user is the correct one
        String identifier = UUID.randomUUID().toString();

        // Create sample user with given identifier
        createUser(new User(identifier, "base-username", "base-password"));

        try {
            userStorage.update(identifier, new User(identifier, "new-username", "new-password"));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Can delete user.")
    void canDeleteUser() {
        // The identifier is used to determine if the updated user is the correct one
        String identifier = UUID.randomUUID().toString();

        // Create sample user with given identifier
        createUser(new User(identifier, "delete-username", "delete-password"));

        try {
            userStorage.delete(identifier);
        } catch (IOException e) {
            fail();
        }

        List<User> users = new ArrayList<>();

        try {
            users = userStorage.readUsers();
        } catch (IOException e) {
            fail();
        }

        // Check if User object with given identifier really was deleted
        for (User user : users) {
            // There still exists a User object with the randomly generated identifier.
            if (user.getUUID().equals(identifier)) {
                fail();
            }
        }
    }

    @Test
    @DisplayName("No users returns null.")
    void noUsersStoredReturnsNull() {
        List<User> users = new ArrayList<>();

        // Clear user storage before validating the return type when user storage is empty.
        clearUserStorage();

        try {
            users = userStorage.readUsers();
        } catch (IOException e) {
            fail();
        }

        // Because we have not added any users to the local storage, the returned value of
        // userStorage.readUsers() should be null.
        Assertions.assertNull(users);
    }

    /**
     * Empties the JSON data in file at the storage path.
     * Used before validating the return type when user storage is empty.
     */
    private void clearUserStorage() {
        try (FileWriter writer = new FileWriter(userStorage.getStoragePath())) {
            writer.write("");
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Has correct storage path.")
    void hasCorrectStoragePath() {
        Assertions.assertEquals(
                String.valueOf(Paths.get(System.getProperty("user.home"), "it1901-gr2103", "cognition", "usersTest.json")),
                userStorage.getStoragePath()
        );
    }
}

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

public class CognitionStorageTest {
    private static final char[] characters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private CognitionStorage cognitionStorage;

    @BeforeEach
    void setUp() {
        try {
            cognitionStorage = new CognitionStorage("cognitionTest.json");
        } catch (IOException e) {
            fail();
        }
    }

    @AfterEach
    void tearDown() {
        clearStorage();
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

        // We use this seed when generating the ID in order to get a deterministic result
        String seed = "seed-used-for-testing";
        String id = UUID.nameUUIDFromBytes(seed.getBytes()).toString();

        // Manipulate userId to ensure that not all IDs are equal
        String userId = id.replace(id.charAt(id.length() - 1), 'u');

        User user = new User(userId, "username", "password");

        for (int i = 0; i < NUMBER_OF_QUIZZES; i++) {
            // Manipulate quizId to ensure that not all IDs are equal
            String quizId = id.replace(id.charAt(id.length() - 1), 'q');
            quizId = quizId.replace(quizId.charAt(quizId.length() - 2), characters[i % characters.length]);

            Quiz quiz = new Quiz(quizId, "quiz-" + i, "description-" + i);

            for (int j = 0; j < NUMBER_OF_FLASHCARDS_PER_QUIZ; j++) {
                // Manipulate flashcardId to ensure that not all IDs are equal
                String flashcardId = id.replace(id.charAt(id.length() - 1), 'f');
                flashcardId = flashcardId.replace(flashcardId.charAt(flashcardId.length() - 2), characters[j % characters.length]);

                Flashcard flashcard = new Flashcard(flashcardId, "front-" + j, "answer-" + j);

                quiz.addFlashcard(flashcard);
            }

            user.addQuiz(quiz);
        }

        try {
            cognitionStorage.create(user);
        } catch (IOException e) {
            fail();
        }

        // Read content of user storage as pure String
        String actual = "";
        try {
            actual = Files.readString(Path.of(cognitionStorage.getStoragePath()));
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
                         "UUID": "4efd2ea4-1598-3ec5-bc09-09ffc0u6251u",
                         "username": "username",
                         "password": "password",
                         "quizzes": [
                             {
                                 "UUID": "4efd2ea4-a598-3ec5-bc09-09ffc0q625aq",
                                 "name": "quiz-0",
                                 "description": "description-0",
                                 "flashcards": [
                                     {
                                         "UUID": "4efd2ea4-a598-3ec5-bc09-09ffc0f625af",
                                         "front": "front-0",
                                         "answer": "answer-0"
                                     },
                                     {
                                         "UUID": "4efd2ea4-b598-3ec5-bc09-09ffc0f625bf",
                                         "front": "front-1",
                                         "answer": "answer-1"
                                     }
                                 ]
                             }
                         ]
                     }
                 ]""".replaceAll("\\s+", "");
    }

    @Test
    @DisplayName("Can read user by username.")
    void canReadUserByUsername() {
        String username = "test-username";
        String password = "test-password";
        User user = new User(UUID.randomUUID().toString(), username, password);

        try {
            cognitionStorage.create(user);
        } catch (IOException e) {
            fail();
        }

        try {
            User parsedUser = cognitionStorage.readByUsername(username);
            Assertions.assertEquals(user, parsedUser);
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * An internal method used during testing due to many tests requiring the creation of a test user.
     *
     * @param user is the user to create
     */
    private void createUser(User user) {
        // Try to create a sample user
        try {
            cognitionStorage.create(user);
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
            user = cognitionStorage.read(identifier);
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
            cognitionStorage.update(identifier, new User(identifier, "new-username", "new-password"));
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
            cognitionStorage.delete(identifier);
        } catch (IOException e) {
            fail();
        }

        List<User> users = new ArrayList<>();

        try {
            users = cognitionStorage.readUsers();
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
        clearStorage();

        try {
            users = cognitionStorage.readUsers();
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
    private void clearStorage() {
        try (FileWriter writer = new FileWriter(cognitionStorage.getStoragePath())) {
            writer.write("");
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Has correct storage path.")
    void hasCorrectStoragePath() {
        Assertions.assertEquals(
                String.valueOf(Paths.get(System.getProperty("user.home"), "it1901-gr2103", "cognition", "cognitionTest.json")),
                cognitionStorage.getStoragePath()
        );
    }
}

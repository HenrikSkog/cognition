package core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

public class UserTest {
    private User user;
    private final String validUsername = "valid-username";
    private final String validPassword = "valid-password";

    @BeforeEach
    void setUp() {
        user = new User(UUID.randomUUID().toString(), validUsername, validPassword);
    }

    @Test
    @DisplayName("Can initialize user.")
    void canInitializeUser() {
        User emptyUser = new User();
        User user = new User(UUID.randomUUID().toString(), "username", "password");

        // If we get here, initializing was successful
    }

    @Test
    @DisplayName("No duplicate quizzes.")
    void noDuplicateQuizzes() {
        Quiz quiz = new Quiz(UUID.randomUUID().toString(), "name", "description");

        // Add quiz for the first time
        user.addQuiz(quiz);

        // Add quiz for the second time. This time, it should not be added.
        user.addQuiz(quiz);

        // Flashcards should only be added once
        Assertions.assertEquals(1, user.getQuizzes().size());
    }

    @Test
    @DisplayName("Can add quiz.")
    void canAddQuiz() {
        user.addQuiz(null);

        // Quiz should not have been added
        Assertions.assertEquals(0, user.getQuizzes().size());

        user.addQuiz(new Quiz(UUID.randomUUID().toString(), "name", "description"));

        // Quiz should have been added
        Assertions.assertEquals(1, user.getQuizzes().size());
    }

    @Test
    @DisplayName("Can remove quiz.")
    void canRemoveQuiz() {
        Quiz nonExistingQuiz = new Quiz(
                UUID.randomUUID().toString(),
                "non-existing-name",
                "non-existing-description"
        );
        Quiz existingQuiz = new Quiz(
                UUID.randomUUID().toString(),
                "existing-name",
                "existing-description"
        );

        // Add flashcard and verify number of flashcards
        user.addQuiz(existingQuiz);
        Assertions.assertEquals(1, user.getQuizzes().size());

        // Remove non-existing flashcard and verify that size is unchanged
        user.removeQuiz(nonExistingQuiz);
        Assertions.assertEquals(1, user.getQuizzes().size());

        // Remove existing flashcard, and verify that size decrease by 1
        user.removeQuiz(existingQuiz);
        Assertions.assertEquals(0, user.getQuizzes().size());
    }

    @Test
    @DisplayName("Can set username.")
    void canSetUsername() {
        String currentUsername = user.getUsername();
        user.setUsername("");

        // Verify that the name is unchanged when provided with an empty string
        Assertions.assertEquals(currentUsername, user.getUsername());

        String newName = "new-name";
        user.setUsername(newName);

        // Verify that the name is changed when provided with a new name
        Assertions.assertEquals(user.getUsername(), newName);
    }

    @Test
    @DisplayName("Can set password.")
    void canSetPassword() {
        String currentPassword = user.getPassword();
        user.setPassword("");

        // Verify that the password is unchanged when provided with an empty string
        Assertions.assertEquals(currentPassword, user.getPassword());

        String newPassword = "new-password";
        user.setPassword(newPassword);

        // Verify that the name is changed when provided with a new name
        Assertions.assertEquals(user.getPassword(), newPassword);
    }

    @Test
    @DisplayName("Displays correct toString.")
    void displaysCorrectToString() {
        String UUID = user.getUUID();
        String username = user.getUsername();
        String password = user.getPassword();
        List<Quiz> quizzes = user.getQuizzes();

        String expected = "User{" +
                "UUID='" + UUID + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", quizzes=" + quizzes +
                '}';

        Assertions.assertEquals(expected, user.toString());
    }

    @Test
    @DisplayName("Can compare with another User object.")
    void canCompareWithAnotherUserObject() {
        User firstUser = new User(UUID.randomUUID().toString(), "first-username", "first-password");
        User secondUser = new User(UUID.randomUUID().toString(), "second-username", "second-password");

        Assertions.assertEquals(firstUser, firstUser);
        Assertions.assertNotEquals(firstUser, null);
    }
}

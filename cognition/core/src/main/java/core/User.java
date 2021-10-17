package core;

import core.validators.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private String UUID;
    private String username;
    private String password;
    private final List<Quiz> quizzes = new ArrayList<>();

    public User() {
    }

    public User(String UUID, String username, String password) {
        setUUID(UUID);
        setUsername(username);
        setPassword(password);
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void updateQuiz(Quiz updatedQuiz) {
        for (Quiz quiz : quizzes) {
            if (quiz.getUUID().equals(updatedQuiz.getUUID())) {
                quizzes.remove(quiz);
                quizzes.add(updatedQuiz);
            }
        }
    }

    private void setUUID(String UUID) {
        if (!Validator.isValidUUID(UUID)) {
            throw new IllegalArgumentException();
        }

        this.UUID = UUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return Objects.equals(UUID, user.UUID) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(quizzes, user.quizzes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UUID, username, password, quizzes);
    }

    public void setUsername(String username) {
        if (!User.isValidUsername(username)) {
            throw new IllegalArgumentException();
        }

        this.username = username;
    }

    public static boolean isValidUsername(String username) {
        return username != null && username.length() >= 3;
    }

    public String getUUID() {
        return UUID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void addQuiz(Quiz quiz) {
        if (quiz == null) {
            return;
        }

        if (!quizzes.contains(quiz)) {
            quizzes.add(quiz);
        }
    }

    public void removeQuiz(Quiz quiz) {
        // Removes only if it is present. Thus, not conditional is needed.
        quizzes.remove(quiz);
    }

    public void setPassword(String password) {
        if (!User.isValidPassword(password)) {
            throw new IllegalArgumentException();
        }

        this.password = password;
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }


    @Override
    public String toString() {
        return "User{" +
                "UUID='" + UUID + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", quizzes=" + quizzes +
                '}';
    }
}

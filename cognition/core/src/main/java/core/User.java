package core;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String UUID;
    private String username;
    private String password;
    private List<Quiz> quizzes = new ArrayList<>();

    public User() {
    }

    public User(String UUID, String username, String password) {

        this.UUID = UUID;
        this.username = username;
        setPassword(password);
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public void setUsername(String username) {
        this.username = username;
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
        quizzes.add(quiz);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
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

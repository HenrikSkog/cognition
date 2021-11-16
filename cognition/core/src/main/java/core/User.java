package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class responsible for handling users.
 */
public class User {
  private final List<Quiz> quizzes = new ArrayList<>();
  private String username;
  private String password;

  public User() {
  }

  /**
   * Initialize user fields.
   *
   * @param username username
   * @param password password
   */
  public User(String username, String password) {
    setUsername(username);
    setPassword(password);
  }

  public static UserValidation isValidUsername(String username) {
    int MAX_INPUT_LENGTH = 36;

    // Checks if username is only white space
    boolean isOnlyWhitespaces = username.trim().equals("");
    // Checks if username only includes letters and numbers
    boolean hasIllegalCharacters = !(username.matches("^[a-zA-Z0-9A-]*"));
    // Checks if username has a length above 3 char and under 36;
    boolean isValidLength = username.length() >= 3 && username.length() < MAX_INPUT_LENGTH;

    if (isOnlyWhitespaces || !isValidLength) {
      return UserValidation.ILLEGAL_INPUT_LENGTH;
    }

    if (hasIllegalCharacters) {
      return UserValidation.ILLEGAL_INPUT;
    }

    return UserValidation.OK;
  }

  public static UserValidation isValidPassword(String password) {
    boolean hasOnlyWhitespaces = password.trim().equals("");

    if (password.length() < 6) {
      return UserValidation.ILLEGAL_INPUT_LENGTH;
    }

    if (hasOnlyWhitespaces) {
      return UserValidation.ILLEGAL_INPUT;
    }

    return UserValidation.OK;
  }

  /**
   * Gets a copy of the quizzes.
   *
   * @return a copy of the quizzes.
   */
  public List<Quiz> getQuizzes() {
    return new ArrayList<>(quizzes);
  }

  /**
   * Update quiz. Takes in a quiz with id equal to one of the existing quizzes,
   * and interchanges them.
   *
   * @param updatedQuiz updated version of quiz
   */
  public void updateQuiz(Quiz updatedQuiz) {
    for (int i = 0; i < quizzes.size(); i++) {
      Quiz quiz = quizzes.get(i);

      if (quiz.getUuid().equals(updatedQuiz.getUuid())) {
        quizzes.remove(quiz);
        quizzes.add(updatedQuiz);
      }
    }
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
    return Objects.equals(username, user.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username);
  }

  public String getUsername() {
    return username;
  }

  /**
   * Sets username.
   *
   * @param username new username
   */
  public void setUsername(String username) {
    if (!(User.isValidUsername(username) == UserValidation.OK)) {
      throw new IllegalArgumentException();
    }

    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  /**
   * Sets password.
   *
   * @param password new password
   */
  public void setPassword(String password) {
    if (!(User.isValidPassword(password) == UserValidation.OK)) {
      throw new IllegalArgumentException();
    }

    this.password = password;
  }

  /**
   * Adds new quiz to quizzes field.
   *
   * @param quiz new quiz.
   */
  public void addQuiz(Quiz quiz) {
    if (quiz == null) {
      throw new IllegalArgumentException("Quiz cannot be null");
    }

    if (!quizzes.contains(quiz)) {
      quizzes.add(quiz);
    }
  }

  public void removeQuiz(Quiz quiz) {
    for (int i = 0; i < quizzes.size(); i++) {
      if (quizzes.get(i).getUuid().equals(quiz.getUuid())) {
        quizzes.remove(i);
        return;
      }
    }
  }

  @Override
  public String toString() {
    return "User{"
            + "quizzes=" + quizzes
            + ", username='" + username + '\''
            + ", password='" + password + '\''
            + '}';
  }
}

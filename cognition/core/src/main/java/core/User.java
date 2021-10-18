package core;

import core.validators.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class responsible for handling users.
 */
public class User {
  private final List<Quiz> quizzes = new ArrayList<>();
  private String uuid;
  private String username;
  private String password;

  public User() {
  }

  /**
   * Initialize user fields.
   *
   * @param uuid     user id
   * @param username username
   * @param password password
   */
  public User(String uuid, String username, String password) {
    setUuid(uuid);
    setUsername(username);
    setPassword(password);
  }

  public static boolean isValidUsername(String username) {
    return username != null && username.length() >= 3;
  }

  public static boolean isValidPassword(String password) {
    return password != null && password.length() >= 6;
  }

  public List<Quiz> getQuizzes() {
    return quizzes;
  }

  /**
   * Update quiz. Takes in a quiz with id equal to one of the existing quizzes,
   * and interchanges them.
   *
   * @param updatedQuiz updated version of quiz
   */
  public void updateQuiz(Quiz updatedQuiz) {
    for (Quiz quiz : quizzes) {
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
    return Objects.equals(uuid, user.uuid) && Objects.equals(username, user.username)
        && Objects.equals(password, user.password) && Objects.equals(quizzes, user.quizzes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, username, password, quizzes);
  }

  public String getUuid() {
    return uuid;
  }

  /**
   * Sets user id.
   *
   * @param uuid new id
   */
  private void setUuid(String uuid) {
    if (!Validator.isValidUuid(uuid)) {
      throw new IllegalArgumentException();
    }

    this.uuid = uuid;
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
    if (!User.isValidUsername(username)) {
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
    if (!User.isValidPassword(password)) {
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

  @Override
  public String toString() {
    return "User{" + "uuid='" + uuid + '\'' + ", username='" + username + '\''
        + ", password='" + password + '\''
        + ", quizzes=" + quizzes + '}';
  }
}

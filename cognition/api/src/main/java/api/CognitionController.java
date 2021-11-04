package api;

import core.Quiz;
import core.User;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import json.CognitionStorage;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * CognitionController controls the REST API logic.
 * Uses Spring Boot.
 */
@RestController
public class CognitionController {
  /**
   * CognitionStorage is the local storage used to implement persistent storage.
   */
  private CognitionStorage cognitionStorage;

  /**
   * Default constructor initializes the application persistent storage.
   *
   * @throws IOException if an error occurs when initializing persistent storage.
   */
  public CognitionController() throws IOException {

    boolean isTest = System.getProperty("webRequestTest") != null;

    /*
     * If system property webRequestTest (indicating API test) is set,
     * or the RestApplication's testMode flag is set, use the test
     * storage file: cognitionTest.json.
     */
    if (isTest || RestApplication.testMode) {
      setCognitionStorage(new CognitionStorage("cognitionTest.json"));
    } else {
      setCognitionStorage(new CognitionStorage());
    }

  }


  /**
   * Sets a new active instance of the persistent storage.
   *
   * @param cognitionStorage is an instance of the CognitionStorage class.
   */
  public void setCognitionStorage(CognitionStorage cognitionStorage) {
    this.cognitionStorage = cognitionStorage;
  }

  /**
   * Performs a GET request to get a list of all users.
   *
   * @return a list of all users.
   * @throws UserNotFoundException if no users are found.
   */
  @GetMapping("/users")
  public List<User> getUsers() throws UserNotFoundException {
    try {
      return cognitionStorage.readUsers();
    } catch (IOException e) {
      throw new UserNotFoundException();
    }
  }

  /**
   * Performs a GET request to retrieve a user
   * if the username corresponds to a valid user.
   *
   * @param username is the String representation of the user's username.
   * @return an instance of the User class.
   * @throws UserNotFoundException if the user cannot be found.
   */
  @GetMapping("/users/{username}")
  public User getUserByUsername(@PathVariable String username) throws UserNotFoundException {
    try {
      return cognitionStorage.read(username);
    } catch (IOException e) {
      throw new UserNotFoundException(
              "No user with the following identifier was found: " + username
      );
    }
  }

  /**
   * Performs a POST request, where the body
   * of the HTTP request corresponds to the JSON representation
   * of a User object.
   *
   * @param user is a User object.
   * @throws StorageException                if an error occurred with the persistent storage
   * @throws IdentifierAlreadyInUseException if an object with the identifier already exists.
   */
  @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void createUser(@RequestBody User user)
          throws StorageException, IdentifierAlreadyInUseException {
    try {
      boolean userAlreadyExists;

      List<User> users = cognitionStorage.readUsers();

      if (users != null) {
        // Check for duplicates if we have content in local storage
        userAlreadyExists = users.stream()
                .anyMatch(u -> u.getUsername().equals(user.getUsername()));
      } else {
        // If local storage is empty, then a duplicate user cannot exist
        userAlreadyExists = false;
      }

      if (userAlreadyExists) {
        throw new IdentifierAlreadyInUseException(user.getUsername());
      } else {
        cognitionStorage.create(user);
      }
    } catch (IOException e) {
      // An error occurred in persistent storage
      throw new StorageException();
    }
  }

  /**
   * Performs a PUT request that updates a
   * user based on the provided parameters.
   *
   * @param user     is the new and updated User.
   * @throws UserNotFoundException if the user cannot be found.
   */
  @PutMapping(value = "/users",
          consumes = MediaType.APPLICATION_JSON_VALUE)
  public void updateUser(@RequestBody User user)
          throws UserNotFoundException {
    try {
      cognitionStorage.update(user.getUsername(), user);
    } catch (IOException e) {
      throw new UserNotFoundException(
              "No user with the following identifier was found: " + user.getUsername()
      );
    }
  }

  /**
   * Performs a DELETE request that deletes a
   * user based on the provided parameters.
   *
   * @param username is the string representation of the user to update.
   * @throws UserNotFoundException if the user cannot be found.
   */
  @DeleteMapping("/users/{username}")
  public void deleteUser(@PathVariable String username) throws UserNotFoundException {
    try {
      cognitionStorage.delete(username);
    } catch (IOException e) {
      throw new UserNotFoundException(
              "No user with the following identifier was found: " + username
      );
    }
  }

  /**
   * Performs a GET request that returns a list
   * of quizzes belonging to a user based on supplied username.
   *
   * @param username is a String representation of the current User's username.
   * @return a list of quizzes corresponding to the current user.
   * @throws UserNotFoundException if no user is found.
   */
  @GetMapping("/quizzes/{username}")
  public List<Quiz> getQuizzesByUsername(@PathVariable String username)
          throws UserNotFoundException {
    try {
      return getUserByUsername(username).getQuizzes();
    } catch (UserNotFoundException e) {
      throw new UserNotFoundException(
              "No user with the following identifier was found: " + username
      );
    }
  }

  /**
   * Performs a GET request and gets a quiz based on the provided UUID.
   *
   * @param uuid is a string corresponding to the UUID of a quiz
   * @return a quiz
   * @throws QuizNotFoundException if no quizzes were found.
   */
  @GetMapping("/quiz/{uuid}")
  public Quiz getQuizByUuid(@PathVariable String uuid) throws QuizNotFoundException {

    List<Quiz> quizzes = getQuizzes();

    if (quizzes.size() == 0) {
      throw new QuizNotFoundException("No quizzes were found in persistent storage.");
    }

    Quiz quiz = quizzes.stream().filter(q -> q.getUuid().equals(uuid)).findFirst().orElse(null);

    if (quiz != null) {
      return quiz;
    } else {
      throw new QuizNotFoundException("No quiz with the following identifier was found: " + uuid);
    }
  }

  /**
   * Performs a PUT request that updates
   * a quiz (replaces it) with a new Quiz object based on
   * the UUID of the quiz.
   *
   * @param newQuiz is the new quiz object.
   * @throws UserNotFoundException if the User belonging to the quiz to updated
   *                               could not be found.
   */
  @PutMapping("/quiz")
  public void updateQuizByUuid(@RequestBody Quiz newQuiz) throws UserNotFoundException {
    User userToUpdate = null;

    for (User user : getUsers()) {
      for (Quiz quiz : user.getQuizzes()) {
        if (quiz.getUuid().equals(newQuiz.getUuid())) {
          userToUpdate = user;
          break;
        }
      }
    }

    if (userToUpdate == null) {
      throw new UserNotFoundException("User belonging to the quiz could not be found.");
    }

    try {
      userToUpdate.updateQuiz(newQuiz);
      cognitionStorage.update(userToUpdate.getUsername(), userToUpdate);
    } catch (IOException e) {
      throw new QuizNotFoundException(
              "No quiz with the following identifier was found: " + newQuiz.getUuid()
      );
    }
  }

  /**
   * Performs a DELETE request which
   * deletes a quiz from persistent storage based on the provided UUID of the quiz.
   *
   * @param uuid is the UUID of the quiz.
   */
  @DeleteMapping("/quiz/{uuid}")
  public void deleteQuizByUuid(@PathVariable String uuid) throws QuizNotFoundException {

    User user = null;
    Quiz quiz = null;

    // Find user and quiz to delete
    for (User u : getUsers()) {
      List<Quiz> quizzes = u.getQuizzes();
      for (Quiz q : quizzes) {
        if (q.getUuid().equals(uuid)) {
          user = u;
          quiz = q;
        }
      }
    }

    if (quiz == null) {
      throw new QuizNotFoundException("No quiz with the following identifier was found: " + uuid);
    }

    try {
      user.removeQuiz(quiz);
      cognitionStorage.update(user.getUsername(), user);
    } catch (IOException e) {
      throw new QuizNotFoundException("No quiz with the following identifier was found: " + uuid);
    }
  }

  /**
   * Performs a POST request which stores a new quiz in the persistent storage.
   *
   * @param quiz     is the quiz object to be stored.
   * @param username is the user that the quiz is related to.
   * @throws QuizNotFoundException           if an error occurred when checking for
   *                                         duplicate quizzes
   * @throws UserNotFoundException           if no user with the given username exists
   * @throws IdentifierAlreadyInUseException if the exact quiz provided already exists
   */
  @PostMapping("/quiz/{username}")
  public void createQuiz(@RequestBody Quiz quiz, @PathVariable String username)
          throws QuizNotFoundException, UserNotFoundException, IdentifierAlreadyInUseException {

    User user = getUserByUsername(username);

    List<Quiz> quizzes = getQuizzes();

    boolean uuidAlreadyExists = quizzes.stream()
            .anyMatch(q -> q.getUuid().equals(quiz.getUuid()));

    if (user == null) {
      throw new UserNotFoundException(username);
    }

    if (uuidAlreadyExists) {
      throw new IdentifierAlreadyInUseException(quiz.getUuid());
    }

    user.addQuiz(quiz);

    try {
      cognitionStorage.update(user.getUsername(), user);
    } catch (IOException e) {
      throw new UserNotFoundException(username);
    }
  }

  /**
   * Gets a list of all available quizzes.
   *
   * @return a list of all quizzes, or null if an error occurs when getting quizzes.
   * @throws QuizNotFoundException if no quizzes were found in persistent storage.
   */
  private List<Quiz> getQuizzes() throws QuizNotFoundException {

    List<Quiz> quizzes = null;

    try {
      quizzes = cognitionStorage.readUsers().stream()
              .flatMap(u -> u.getQuizzes().stream()).collect(Collectors.toList());
    } catch (IOException e) {
      throw new QuizNotFoundException("No quizzes were found in persistent storage.");
    }

    return quizzes;
  }

}

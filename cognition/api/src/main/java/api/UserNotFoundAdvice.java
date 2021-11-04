package api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for when the user was not found on the server.
 */
@ControllerAdvice
public class UserNotFoundAdvice {

  /**
   * Method that returns the exception message.
   *
   * @param exception is the exception thrown
   * @return a String representation of the exception message
   */
  @ResponseBody
  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String userNotFoundHandler(UserNotFoundException exception) {
    return exception.getMessage();
  }
}

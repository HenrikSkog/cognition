package core.validators;

/**
 * Validator class with static validator methods.
 */
public class Validator {
  public static boolean isValidUuid(String uuid) {
    return uuid.length() == 36 && uuid.split("-").length == 5;
  }
}

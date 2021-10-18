package core.tools;

/**
 * Class with static helper functions.
 */
public class Tools {
  public Tools() {
  }

  public static String capitalize(String string) {
    return string.substring(0, 1).toUpperCase() + string.substring(1);
  }
}

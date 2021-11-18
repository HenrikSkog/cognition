package core;

import core.tools.Tools;

/**
 * A CompactQuiz is a struct used as a "lighter" version of a Quiz object.
 * When fetching all quizzes for the current user, we only need to display the quiz titles
 * and keep track of the corresponding identifier.
 * Thus, we first fetch a CompactQuiz, and then later fetch a complete Quiz on-demand.
 */
public class CompactQuiz {
  private String uuid;
  private String name;

  public CompactQuiz(String uuid, String name) {
    setUuid(uuid);
    setName(name);
  }

  private void setUuid(String uuid) {
    if (!Tools.isValidUuid(uuid)) {
      throw new IllegalArgumentException();
    }

    this.uuid = uuid;
  }

  private void setName(String name) {
    if (!Quiz.isValidName(name)) {
      throw new IllegalArgumentException();
    }

    this.name = name;
  }

  public String getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "CompactQuiz{"
        + "uuid='" + uuid + '\''
        + ", name='" + name + '\''
        + '}';
  }
}

package json;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * An abstract class for controlling persistent storage.
 *
 * @param <T> is a generic type that the implementor class decides.
 */
public abstract class Storage<T> implements Storable<T> {
  private final Gson gson = new Gson();
  private String storagePath;

  /**
   * Creates a file if it does not already exist with the filename given.
   *
   * @param filename is the name of the file to be created.
   * @throws IOException if an error occurs while directories for local storage. A
   *                     potential exception is handled in the frontend.
   */
  public Storage(String filename) throws IOException {
    if (filename != null) {
      setStoragePath(filename);
    }

    // A potential exception is handled in the frontend.
    createDirectoryIfNotExists();
  }

  public String getStoragePath() {
    return storagePath;
  }

  /**
   * Sets the storage path of the JSON data.
   *
   * @param filename is the filename of the JSON data
   */
  public void setStoragePath(String filename) {
    storagePath = String.valueOf(
            Paths.get(System.getProperty("user.home"), "it1901-gr2103", "cognition", filename));
  }

  public Gson getGson() {
    return gson;
  }

  /**
   * Checks if the storage is empty.
   *
   * @return a boolean indicating if the storage is empty.
   */
  public boolean isEmpty() {
    File file = new File(getStoragePath());

    // file.length is 0 if file does not exist or has no content
    return file.length() == 0;
  }

  @Override
  public abstract void create(T instance) throws IOException;

  @Override
  public abstract T read(String identifier) throws IOException;

  @Override
  public abstract void update(String identifier, T instance) throws IOException;

  @Override
  public abstract void delete(String identifier) throws IOException;

  /**
   * Creates the directories required if they do not exist.
   *
   * @throws IOException if an error occurred when trying to create the
   *                     directories
   */
  private void createDirectoryIfNotExists() throws IOException {
    try {
      Path path = Paths.get(System.getProperty("user.home"), "it1901-gr2103", "cognition");
      Files.createDirectories(path);
    } catch (IOException e) {
      throw new IOException("An error occurred when trying to create directory: " + storagePath);
    }
  }
}

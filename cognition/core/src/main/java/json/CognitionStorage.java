package json;

import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import core.User;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;

public class CognitionStorage extends Storage<User> {

    public CognitionStorage() throws IOException {
        super("cognition.json");
    }

    public CognitionStorage(String filename) throws IOException {
        super(filename);
    }

    /**
     * Reads all users from storage and deserializes the JSON array to a list of User objects
     *
     * @return a List of User objects
     * @throws IOException if an error occurred when trying to read from the storage file
     */
    public List<User> readUsers() throws IOException {
        if (isEmpty()) {
            return null;
        }

        try {
            String content = Files.readString(Path.of(getStoragePath()), StandardCharsets.UTF_8);
            return getGson().fromJson(
                    content,
                    new TypeToken<List<User>>() {
                    }.getType()
            );
        } catch (IOException e) {
            throw new IOException(getStoragePath() + " is present, but an error occurred when reading all users from user storage.");
        }
    }

    /**
     * Writes a list of User objects to local storage.
     *
     * @param users is a list of User objects.
     * @throws IOException     if an error occurred when trying to write to local storage.
     * @throws JsonIOException if an error occurred when serializing the JSON content.
     */
    private void writeToJson(List<User> users) throws JsonIOException, IOException {
        try (FileWriter writer = new FileWriter(getStoragePath(), StandardCharsets.UTF_8)) {
            try {
                getGson().toJson(users, writer);
            } catch (JsonIOException e) {
                writer.flush();
                writer.close();
                throw new JsonIOException("An error occurred when serializing the JSON content.");
            }

            writer.flush();
        }
    }

    /**
     * Reads all users from file, appends the provided user, and then overwrites local storage file.
     * Users are stored in an array.
     *
     * @param instance is the user that should be written to file
     */
    @Override
    public void create(User instance) throws IOException {
        List<User> newUsers;

        List<User> users = readUsers();

        if (users == null) {
            // To maintain consistency in the dataset, a single user is also stored in a list
            newUsers = List.of(instance);
        } else {
            users.add(instance);
            newUsers = users;
        }

        writeToJson(newUsers);
    }

    @Override
    public User read(String identifier) throws IOException, NullPointerException {
        List<User> users = readUsers();

        if (users == null) {
            throw new NullPointerException("No users in local storage.");
        }

        // Filters based on id and returns null if no match was found
        return users.stream()
                .filter(user -> user.getUUID().equals(identifier))
                .findFirst()
                .orElse(null);
    }

    public User readByUsername(String username) throws IOException, NullPointerException {
        List<User> users = readUsers();

        if (users == null) {
            throw new NullPointerException("No users in local storage.");
        }

        // Filters based on id and returns null if no match was found
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds a user based on an identifier and lets a consumer modify the user list based on it.
     * Writes the new user list to file.
     *
     * @param identifier user that should be taken action upon
     * @param action     consumer that specifies the action
     * @throws IOException            if an error occurred when trying to read the User from local storage
     * @throws NullPointerException   if no users exist in local storage
     * @throws NoSuchElementException if no user with the given identifier was found.
     */
    private void updateUser(String identifier, BiConsumer<List<User>, Integer> action) throws IOException, NullPointerException, NoSuchElementException {
        List<User> users = readUsers();

        if (users == null) {
            throw new NullPointerException("No users in local storage.");
        }

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUUID().equals(identifier)) {
                // Accept the provided action, passed in as parameter
                action.accept(users, i);

                writeToJson(users);
                return;
            }
        }

        // If loop is finished, no user with the given identifier was found
        throw new NoSuchElementException("No user with identifier \"" + identifier + "\" was found.");
    }

    @Override
    public void update(String identifier, User instance) throws IOException {
        updateUser(identifier, (users, index) -> {
            // Remove old user
            users.remove((int) index);

            // Add new user
            users.add(index, instance);
        });
    }

    @Override
    public void delete(String identifier) throws IOException {
        updateUser(identifier, ((users, integer) -> {
            // Remove old user
            users.remove((int) integer);
        }));
    }
}

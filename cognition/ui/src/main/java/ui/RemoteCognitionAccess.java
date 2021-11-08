package ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import core.User;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * A REST Controller that creates a bridge between
 * the backend REST API and the frontend.
 */
public class RemoteCognitionAccess {

  private String baseUri = "http://localhost:";
  private final Gson gson = new Gson();
  private final HttpClient client = HttpClient.newHttpClient();

  public RemoteCognitionAccess() {
    this(8080);
  }

  public RemoteCognitionAccess(int port) {
    baseUri += String.valueOf(port);
  }

  /**
   * A method that performs a get request to retrieve
   * a list of all the users on the server.
   *
   * @return a list of users.
   * @throws InterruptedException if no connection is established
   * @throws IOException          if the response is not 200
   */
  public List<User> readUsers() throws InterruptedException, IOException {

    String endpoint = baseUri + "/users";

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(endpoint))
            .build();

    HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

    return gson.fromJson(
            new JsonReader(
                    new StringReader(response.body())
            ),
            new TypeToken<List<User>>() {}.getType());

  }

  /**
   * A method that performs a get request to retrieve
   * a user with the specified username from the server.
   *
   * @param username is the string representation of the username
   * @return the user or null
   * @throws InterruptedException if no connection is established
   * @throws IOException          if the response is not 200
   */
  public User read(String username) throws InterruptedException, IOException {

    String endpoint = baseUri + "/users/" + username;

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(endpoint))
            .build();

    HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

    return gson.fromJson(
            new JsonReader(
                    new StringReader(response.body())
            ),
            new TypeToken<User>() {}.getType());

  }

  /**
   * A method that performs a PUT request to update
   * a given user with a new user object.
   *
   * @param instance is the new User object
   * @throws InterruptedException if no connection is established
   * @throws IOException          if the response is not 200
   */
  public void update(User instance) throws InterruptedException, IOException {

    String endpoint = baseUri + "/users";
    String payload = gson.toJson(instance);


    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(endpoint))
            .headers("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(payload))
            .build();


    client.send(request, HttpResponse.BodyHandlers.ofString());
  }

  /**
   * A method that performs a POST request to create
   * a new user from a new user object.
   *
   * @param instance is the new User object
   * @throws InterruptedException if no connection is established
   * @throws IOException          if the response is not 200
   */
  public void create(User instance) throws InterruptedException, IOException {

    String endpoint = baseUri + "/users";
    String payload = gson.toJson(instance);

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(endpoint))
            .headers("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .build();


    client.send(request,
            HttpResponse.BodyHandlers.ofString());

  }

  /**
   * A method that performs a DELETE request to delete
   * a user based on a username.
   *
   * @param username is the string representation of the username
   * @throws InterruptedException if no connection is established
   * @throws IOException          if the response is not 200
   */
  public void delete(String username) throws InterruptedException, IOException {

    String endpoint = baseUri + "/users/" + username;

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(endpoint))
            .DELETE()
            .build();

    client.send(request,
            HttpResponse.BodyHandlers.ofString());

  }

}

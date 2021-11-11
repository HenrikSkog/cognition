package api;

import java.util.Arrays;
import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Starts the Spring Boot application.
 */
@SpringBootApplication
public class RestApplication {
  private static int PORT = 8080;
  private static boolean testMode = false;


  /**
   * Method that runs the Spring Boot application
   * with specified parameters.
   *
   * @param args are the arguments passed to Spring Boot
   */
  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(RestApplication.class);

    // If Spring Boot arguments contain the word "testmode",
    // set test mode to true.
    if (Arrays.stream(args).toList().contains("testmode")) {
      setTestMode(true);
      PORT = 3000;
    }

    application.setDefaultProperties(
            Collections.singletonMap("server.port", String.valueOf(PORT))
    );

    application.run(args);
  }


  public static void setTestMode(boolean testMode) {
    RestApplication.testMode = testMode;
  }

  public static boolean isTestMode() {
    return testMode;
  }
}

package api;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * Starts the Spring Boot application.
 */
@SpringBootApplication
public class RestApplication {
  public static final int TEST_PORT = 3000;
  private static int PORT = 8080;
  private static boolean testMode = false;

  private static ConfigurableApplicationContext context;

  /**
   * Starts the Spring Boot application.
   *
   * @param args are the optional arguments passed in when starting the Spring Boot application.
   */
  public static void main(String[] args) {
    context = new SpringApplicationBuilder(RestApplication.class).run();
  }

  public static void setTestMode(boolean testMode) {
    RestApplication.testMode = testMode;
  }

  public static boolean isTestMode() {
    return testMode;
  }

  public static void stop() {
    context.stop();
    context.close();
  }
}

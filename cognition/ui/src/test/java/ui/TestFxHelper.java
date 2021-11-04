package ui;

import org.testfx.util.WaitForAsyncUtils;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * TestFxHelper is a package-private class, as it is only used test in the ui module.
 * It serves as a helper class with static methods when running tests in the ui module.
 */
class TestFxHelper {
  /**
   * Waits for the event queue of the "JavaFX Application Thread" to be completed,
   * as well as any new events triggered in it
   */
  protected static void waitForFxEvents() {
    WaitForAsyncUtils.waitForFxEvents();
  }

  protected static void sleep(int seconds) {
    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
      fail();
    }
  }
}

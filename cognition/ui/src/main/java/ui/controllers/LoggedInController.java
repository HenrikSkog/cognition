package ui.controllers;

import core.User;
import json.CognitionStorage;

/**
 * LoggedInController holds state of the application when user
 * is logged in.
 * It extends Controller, as there's common functionality in these controllers.
 */
public abstract class LoggedInController extends Controller {
  private User user;

  public LoggedInController(User user, CognitionStorage cognitionStorage) {
    super(cognitionStorage);
    this.user = user;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Sets the state of the application.
   *
   * @param user is the current user.
   * @param cognitionStorage is the current instance of the local storage.
   */
  public void setState(User user, CognitionStorage cognitionStorage) {
    this.user = user;

    // Controller class method
    setCognitionStorage(cognitionStorage);
  }
}

package ui.controllers;

import core.User;
import json.CognitionStorage;

public abstract class LoggedInController extends Controller {
    private User user;
    public LoggedInController(User user, CognitionStorage cognitionStorage) {
        super(cognitionStorage);
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setState(User user, CognitionStorage cognitionStorage) {
        this.user = user;

        // Controller class method
        setCognitionStorage(cognitionStorage);
    }
}

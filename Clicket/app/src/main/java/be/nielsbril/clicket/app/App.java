package be.nielsbril.clicket.app;

import android.app.Application;

import be.nielsbril.clicket.app.models.User;

public class App extends Application {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
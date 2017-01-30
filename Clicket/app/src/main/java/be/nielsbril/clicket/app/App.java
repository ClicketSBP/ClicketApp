package be.nielsbril.clicket.app;

import android.app.Application;
import android.content.Context;

import be.nielsbril.clicket.app.models.User;
import be.nielsbril.clicket.app.views.ParkFragment;

public class App extends Application {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private static ParkFragment parkFragment;

    public static ParkFragment getParkFragment() {
        return parkFragment;
    }

    public static void setParkFragment(ParkFragment parkFragment) {
        App.parkFragment = parkFragment;
    }

}
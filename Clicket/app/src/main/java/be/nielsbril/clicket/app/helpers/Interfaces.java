package be.nielsbril.clicket.app.helpers;

import android.app.Fragment;

import be.nielsbril.clicket.app.models.Car;

public class Interfaces {

    public interface onAccountRegisteredListener {
        void onAccountRegistered(String email, String token);
    }

    public interface onCarSelectedListener {
        void onCarSelected(Car car);
    }

    public interface changeToolbar {
        void setTitle(String title);
        void toggleNavItems(String tag);
    }

    public interface navigate {
        void navigateFragment(Fragment fragment, String tag);
    }

}
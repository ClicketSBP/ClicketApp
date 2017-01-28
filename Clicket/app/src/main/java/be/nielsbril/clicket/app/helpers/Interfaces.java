package be.nielsbril.clicket.app.helpers;

public class Interfaces {

    public interface onAccountRegisteredListener {
        void onAccountRegistered(String email, String token);
    }

    public interface changeToolbar {
        void setTitle(String title);
    }

}
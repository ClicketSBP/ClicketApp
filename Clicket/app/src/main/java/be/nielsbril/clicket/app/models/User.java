package be.nielsbril.clicket.app.models;

public class User {

    private int _id;

    public User(int id) {
        _id = id;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

}
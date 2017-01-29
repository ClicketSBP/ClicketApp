package be.nielsbril.clicket.app.models;

public class Zone {

    private int _id;
    private String name;
    private double price;

    public Zone(int id, String name, double price) {
        _id = id;
        this.name = name;
        this.price = price;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
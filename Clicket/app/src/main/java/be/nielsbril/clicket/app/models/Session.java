package be.nielsbril.clicket.app.models;

public class Session {

    private int _id;
    private Car car_id;
    private double lat;
    private double lng;
    private User user_id;
    private String started_on;
    private boolean active;
    private String stopped_on;
    private String street;
    private Zone zone_id;

    public Session(int id, Car car_id, double lat, double lng, User user_id, String started_on, boolean active, String stopped_on, String street, Zone zone_id) {
        _id = id;
        this.car_id = car_id;
        this.lat = lat;
        this.lng = lng;
        this.started_on = started_on;
        this.user_id = user_id;
        this.active = active;
        this.stopped_on = stopped_on;
        this.street = street;
        this.zone_id = zone_id;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Car getCar_id() {
        return car_id;
    }

    public void setCar_id(Car car_id) {
        this.car_id = car_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public String getStarted_on() {
        return started_on;
    }

    public void setStarted_on(String started_on) {
        this.started_on = started_on;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getStopped_on() {
        return stopped_on;
    }

    public void setStopped_on(String stopped_on) {
        this.stopped_on = stopped_on;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Zone getZone_id() {
        return zone_id;
    }

    public void setZone_id(Zone zone_id) {
        this.zone_id = zone_id;
    }

}
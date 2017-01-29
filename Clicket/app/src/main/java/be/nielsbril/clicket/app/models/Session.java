package be.nielsbril.clicket.app.models;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Session {

    DateFormat format = new SimpleDateFormat("dd MM yyyy HH:mm:ss", Locale.ENGLISH);

    private int _id;
    private Car car_id;
    private double lat;
    private double lng;
    private User user_id;
    private Date started_on;
    private boolean active;
    private Date stopped_on;
    private Zone zone_id;

    public Session(int id, Car car_id, double lat, double lng, User user_id, String started_on, boolean active, String stopped_on, Zone zone_id) {
        _id = id;
        this.car_id = car_id;
        this.lat = lat;
        this.lng = lng;
        this.user_id = user_id;
        this.active = active;
        this.zone_id = zone_id;

        try {
            this.started_on = format.parse(started_on);
        } catch (ParseException e) {
            Log.d("Error", e.getMessage());
            this.started_on = new Date();
        }

        try {
            this.stopped_on = format.parse(stopped_on);
        } catch (ParseException e) {
            Log.d("Error", e.getMessage());
            this.stopped_on = new Date();
        }
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
        return started_on.toString();
    }

    public void setStarted_on(String started_on) {
        try {
            this.started_on = format.parse(started_on);
        } catch (ParseException e) {
            Log.d("Error", e.getMessage());
            this.started_on = new Date();
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getStopped_on() {
        return stopped_on.toString();
    }

    public void setStopped_on(String stopped_on) {
        try {
            this.stopped_on = format.parse(stopped_on);
        } catch (ParseException e) {
            Log.d("Error", e.getMessage());
            this.stopped_on = new Date();
        }
    }

    public Zone getZone_id() {
        return zone_id;
    }

    public void setZone_id(Zone zone_id) {
        this.zone_id = zone_id;
    }

}
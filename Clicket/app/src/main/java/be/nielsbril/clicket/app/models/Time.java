package be.nielsbril.clicket.app.models;

public class Time {

    private double hoursParked;
    private double minutesParked;

    public Time(double hoursParked, double minutesParked) {
        this.hoursParked = hoursParked;
        this.minutesParked = minutesParked;
    }

    public double getHoursParked() {
        return hoursParked;
    }

    public void setHoursParked(double hoursParked) {
        this.hoursParked = hoursParked;
    }

    public double getMinutesParked() {
        return minutesParked;
    }

    public void setMinutesParked(double minutesParked) {
        this.minutesParked = minutesParked;
    }

}
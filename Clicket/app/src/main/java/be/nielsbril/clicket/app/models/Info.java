package be.nielsbril.clicket.app.models;

public class Info {

    private Time time;
    private Price price;

    public Info(Time time, Price price) {
        this.time = time;
        this.price = price;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

}
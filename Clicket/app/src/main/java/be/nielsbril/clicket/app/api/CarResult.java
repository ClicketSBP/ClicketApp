package be.nielsbril.clicket.app.api;

import java.util.List;

import be.nielsbril.clicket.app.models.Car;

public class CarResult {

    private String info;
    private boolean success;
    private List<Car> data;

    public CarResult(String info, boolean success, List<Car> data) {
        this.info = info;
        this.success = success;
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Car> getData() {
        return data;
    }

    public void setData(List<Car> data) {
        this.data = data;
    }
}
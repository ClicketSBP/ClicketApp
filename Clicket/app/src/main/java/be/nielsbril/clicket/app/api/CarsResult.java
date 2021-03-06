package be.nielsbril.clicket.app.api;

import android.databinding.ObservableArrayList;

import be.nielsbril.clicket.app.models.Car;

public class CarsResult {

    private String info;
    private boolean success;
    private ObservableArrayList<Car> data;

    public CarsResult(String info, boolean success, ObservableArrayList<Car> data) {
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

    public ObservableArrayList<Car> getData() {
        return data;
    }

    public void setData(ObservableArrayList<Car> data) {
        this.data = data;
    }

}
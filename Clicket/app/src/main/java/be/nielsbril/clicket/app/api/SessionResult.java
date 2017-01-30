package be.nielsbril.clicket.app.api;

import android.databinding.ObservableArrayList;

import be.nielsbril.clicket.app.models.Data;

public class SessionResult {

    private String info;
    private boolean success;
    private ObservableArrayList<Data> data;

    public SessionResult(String info, boolean success, ObservableArrayList<Data> data) {
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

    public ObservableArrayList<Data> getData() {
        return data;
    }

    public void setData(ObservableArrayList<Data> data) {
        this.data = data;
    }

}
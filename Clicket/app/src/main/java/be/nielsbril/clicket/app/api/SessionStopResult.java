package be.nielsbril.clicket.app.api;

import be.nielsbril.clicket.app.models.Data;

public class SessionStopResult {

    private String info;
    private boolean success;
    private Data data;

    public SessionStopResult(String info, boolean success, Data data) {
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}

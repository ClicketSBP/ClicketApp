package be.nielsbril.clicket.app.api;

import be.nielsbril.clicket.app.models.Session;

public class SessionSingleResult {

    private String info;
    private boolean success;
    private Session data;

    public SessionSingleResult(String info, boolean success, Session data) {
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

    public Session getData() {
        return data;
    }

    public void setData(Session data) {
        this.data = data;
    }

}
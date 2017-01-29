package be.nielsbril.clicket.app.api;

import be.nielsbril.clicket.app.models.User;

public class UserResult {

    private String info;
    private boolean success;
    private User data;

    public UserResult(String info, boolean success, User data) {
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

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

}
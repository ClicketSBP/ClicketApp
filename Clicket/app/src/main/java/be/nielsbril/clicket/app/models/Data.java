package be.nielsbril.clicket.app.models;

public class Data {

    private Session session;
    private Info info;

    public Data(Session session, Info info) {
        this.session = session;
        this.info = info;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

}
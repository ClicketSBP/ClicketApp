package be.nielsbril.clicket.app.models;

import java.text.DecimalFormat;

public class User {

    private int _id;
    private String email;
    private String name;
    private String firstname;
    private String phone;
    private double invoice_amount;

    public User(int id, String email, String name, String firstname, String phone, double invoice_amount) {
        this._id = id;
        this.email = email;
        this.name = name;
        this.firstname = firstname;
        this.phone = phone;
        this.invoice_amount = invoice_amount;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getInvoice_amount() {
        return invoice_amount;
    }

    public void setInvoice_amount(double invoice_amount) {
        this.invoice_amount = invoice_amount;
    }

}
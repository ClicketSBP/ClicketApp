package be.nielsbril.clicket.app.models;

public class Price {

    private double parkCosts;
    private double transactionCosts;
    private double total;

    private Price(double parkCosts, double transactionCosts, double total) {
        this.parkCosts = parkCosts;
        this.transactionCosts = transactionCosts;
        this.total = Math.round(total * 100.0) / 100.0;
    }

    public double getParkCosts() {
        return parkCosts;
    }

    public void setParkCosts(double parkCosts) {
        this.parkCosts = parkCosts;
    }

    public double getTransactionCosts() {
        return transactionCosts;
    }

    public void setTransactionCosts(double transactionCosts) {
        this.transactionCosts = transactionCosts;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = Math.round(total * 100.0) / 100.0;
    }

}
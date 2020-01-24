package org.cshaifasweng.winter.models;

public class OrderCompensation {
    private double amount;

    public OrderCompensation() {

    }

    public OrderCompensation(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

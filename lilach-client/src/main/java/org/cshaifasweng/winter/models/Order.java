package org.cshaifasweng.winter.models;

public class Order {

    public Order(){
    }

    private long id;

    private boolean delivery;

    private long supplyDate;

    private boolean greeting;

    private String deliveryAddress;

    private long recipientPhone;

    private double price;

    public long getId() { return id; }

    public void SetID(long id) { this.id = id; }

    public void setSupplyDate(long supplyDate)  { this.supplyDate = supplyDate; }

    public void setDelivery(boolean delivery) {this.delivery = delivery; }

    public void setRecipientPhone(long recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public void setGreeting(boolean greeting) {
        this.greeting = greeting;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public boolean isGreeting() {
        return greeting;
    }

    public String getDeliveryAddress() { return deliveryAddress;}

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }


    public long getRecipientPhone() {
        return recipientPhone;
    }

    public long getSupplyDate() {
        return supplyDate;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }


}





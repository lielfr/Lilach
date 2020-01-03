package org.cshaifasweng.winter.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Customer extends User {

    private long creditCard;

    private String customerName;

    public Customer(String email, String password, long creditCard, String customerName) {
        super(email, password);
        this.creditCard = creditCard;
        this.customerName = customerName;
    }

    public Customer() {
    }

    public long getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(long creditCard) {
        this.creditCard = creditCard;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}


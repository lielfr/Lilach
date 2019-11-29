package org.cshaifasweng.winter.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Customer {
    @Id
    private long customerId;

    private long creditCard;

    private String customerName;

    private String password;

    public Customer(long customerId, long creditCard, String customerName, String password) {
        this.customerId = customerId;
        this.creditCard = creditCard;
        this.customerName = customerName;
        this.password = password;
    }

    public Customer() {
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


package org.cshaifasweng.winter.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collection;

@Entity
public class Customer extends User {

    private long creditCard;

    private String customerName;

    public Customer(String email, String password, Collection<Role> roles, long creditCard, String customerName) {
        super(email, password, roles);
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


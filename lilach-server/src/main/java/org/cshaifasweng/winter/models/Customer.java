package org.cshaifasweng.winter.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collection;

@Entity
public class Customer extends User {

    private long creditCard;


    public Customer(String email, String password, String firstName, String lastName, String phone, Collection<Role> roles, long creditCard) {
        super(email, password, firstName, lastName, phone, roles);
        this.creditCard = creditCard;
    }

    public Customer() {
    }

    public long getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(long creditCard) {
        this.creditCard = creditCard;
    }
}


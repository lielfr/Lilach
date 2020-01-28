package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.Customer;

public class CustomerSendEvent {
    private Customer customer;

    public CustomerSendEvent(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}

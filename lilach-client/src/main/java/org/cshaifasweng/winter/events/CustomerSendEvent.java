package org.cshaifasweng.winter.events;

import javafx.stage.Stage;
import org.cshaifasweng.winter.models.Customer;

public class CustomerSendEvent {
    private Customer customer;
    private Stage stage;

    public CustomerSendEvent(Customer customer, Stage stage) {
        this.customer = customer;
        this.stage = stage;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Stage getStage() {
        return stage;
    }
}

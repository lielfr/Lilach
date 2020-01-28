package org.cshaifasweng.winter.events;

import javafx.stage.Stage;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Employee;
import org.cshaifasweng.winter.models.User;

public class CustomerSendEvent {
    private User user;
    private Stage stage;

    public CustomerSendEvent(User user, Stage stage) {
        this.user = user;
        this.stage = stage;
    }

    public Customer getCustomer() {
        if (!(user instanceof Customer)) return null;
        return (Customer) user;
    }

    public Employee getEmployee() {
        if (!(user instanceof Employee)) return null;
        return (Employee) user;
    }

    public Stage getStage() {
        return stage;
    }
}

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
        return (Customer) user;
    }

    public Employee getEmployee() {
        return (Employee) user;
    }

    public Stage getStage() {
        return stage;
    }
}

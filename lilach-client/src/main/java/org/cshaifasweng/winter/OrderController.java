package org.cshaifasweng.winter;

import javafx.fxml.FXML;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;


public class OrderController implements Refreshable {


    @Override
    public void refresh() {
    }

    @FXML
    private void GoToPayment() throws IOException {
        EventBus.getDefault().post(new DashboardSwitchEvent("order_payment"));
    }

    @FXML
    private void GoToOrderCompleted() throws IOException {
        EventBus.getDefault().post(new DashboardSwitchEvent("order_completed"));
    }

    @FXML
    private void BackToEditOrder() throws IOException {
        EventBus.getDefault().post(new DashboardSwitchEvent("order"));
    }

    @FXML
    private void BackToCart() throws IOException {
        EventBus.getDefault().post(new DashboardSwitchEvent("Customer_Cart"));
    }





}

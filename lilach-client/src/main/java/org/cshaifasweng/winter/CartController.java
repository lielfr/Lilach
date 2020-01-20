package org.cshaifasweng.winter;


import javafx.fxml.FXML;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;


public class CartController implements Refreshable {


    @Override
    public void refresh() {
    }

    @FXML
    private void backToCatalog() throws IOException {
        EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
    }

    @FXML
    private void goToLogin() throws IOException {
        EventBus.getDefault().post(new DashboardSwitchEvent("login_screen"));
    }

    @FXML
    private void goToOrder() throws IOException {
        EventBus.getDefault().post(new DashboardSwitchEvent("order"));
    }



}
package org.cshaifasweng.winter;


import javafx.fxml.FXML;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;


public class CartController extends PrimaryController implements Refreshable {

    public boolean CartIsEmpty = true;

    @Override
    public void refresh() {
    }


    @FXML
    private void switchToDashboard() throws IOException {
        EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
    }

    @FXML
    private void switchToLogin() throws IOException {
        EventBus.getDefault().post(new DashboardSwitchEvent("login_screen"));
    }


}
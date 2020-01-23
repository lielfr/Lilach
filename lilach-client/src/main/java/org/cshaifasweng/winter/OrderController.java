package org.cshaifasweng.winter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.models.Order;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;


public class OrderController implements Refreshable {

    public Order order;

    @FXML
    public TextField AddressDetails;

    @FXML
    public Label Address;

    @FXML
    public Button checkout;

    @FXML
    public Label Phone;

    @FXML
    public TextField FillPhone;

    @Override
    public void refresh() {
    }

    @FXML
    public void createOrder(ActionEvent event) {
        LilachService service = APIAccess.getService();
        service.newOrder(order);
    }
    @FXML
    public void CheckOut(ActionEvent actionEvent) {
        EventBus.getDefault().post(new DashboardSwitchEvent("order_payment"));
    }

    public void backToEditOrder(MouseEvent mouseEvent) {
        EventBus.getDefault().post(new DashboardSwitchEvent("order"));

    }
}


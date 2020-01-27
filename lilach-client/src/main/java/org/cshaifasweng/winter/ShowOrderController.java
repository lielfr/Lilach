package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.cshaifasweng.winter.events.OrderShowEvent;
import org.cshaifasweng.winter.models.Order;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.Subscribe;

public class ShowOrderController implements Refreshable {

    @FXML
    private Button exitButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label greetingLabel;

    @FXML
    private Label deliveryAddressLabel;

    @FXML
    private Label recipientLabel;

    @FXML
    private Label deliveryDateLabel;

    @FXML
    private Label priceLabel;


    private Order currentOrder;

    @Subscribe
    public void handleEvent(OrderShowEvent event){
    currentOrder = event.getOrder();
    firstNameLabel.setText(currentOrder.getOrderedBy().getFirstName());
    lastNameLabel.setText(currentOrder.getOrderedBy().getLastName());
    idLabel.setText(currentOrder.getOrderedBy().getMisparZehut());
    emailLabel.setText(currentOrder.getOrderedBy().getEmail());
    phoneLabel.setText(currentOrder.getOrderedBy().getPhone());
    addressLabel.setText(currentOrder.getOrderedBy().getAddress());
    greetingLabel.setText(currentOrder.getGreeting());
    deliveryAddressLabel.setText(currentOrder.getDeliveryAddress());
    recipientLabel.setText(currentOrder.getRecipientMail());
    priceLabel.setText(Double.toString(currentOrder.getPrice()));
    }

    @FXML
    void cancelOrder(ActionEvent event) {
        LilachService service = APIAccess.getService();
        service.cancelOrder(currentOrder.getId());

    }

    @FXML
    void exitScreen(ActionEvent event) {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void onSwitch() {

    }
}

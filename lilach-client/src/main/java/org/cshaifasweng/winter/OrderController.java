package org.cshaifasweng.winter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.awt.event.MouseEvent;


public class OrderController implements Refreshable {

/*
    @FXML
    public TextField Right;

    @FXML
    private TextField orderDeliveryAddress;
*/

    @Override
    public void refresh() {
    }


    @FXML
    private Button displayOrderAddress;

    @FXML
    private TextArea orderDeliveryAddress;

    @FXML
    private TextArea Right;

    @FXML
    void backToCart(MouseEvent event) {

    }

    @FXML
    void goToPayment(MouseEvent event) {

    }

    @FXML
    void move(ActionEvent event) {
        Right.setText(orderDeliveryAddress.getText());

    }


/*    @FXML
    void goToPayment(MouseEvent event){
        EventBus.getDefault().post(new DashboardSwitchEvent("order_payment"));
        Right.setText(orderDeliveryAddress.getText());
    }*/

 /*   @FXML
    void move(MouseEvent event){
        EventBus.getDefault().post(new DashboardSwitchEvent("order_payment"));
        Right.setText(orderDeliveryAddress.getText());
    }


    @FXML
    void goToOrderCompleted(MouseEvent event){
        EventBus.getDefault().post(new DashboardSwitchEvent("order_completed"));
    }

    @FXML
    void backToEditOrder(MouseEvent event){
        EventBus.getDefault().post(new DashboardSwitchEvent("order"));
    }

    @FXML
    void backToCart(MouseEvent event){
        EventBus.getDefault().post(new DashboardSwitchEvent("Customer_Cart"));
    }
*/
}



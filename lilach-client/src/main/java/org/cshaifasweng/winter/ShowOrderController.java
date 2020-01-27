package org.cshaifasweng.winter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
    private Label phoneLabel;

    @FXML
    private Label addressLable;

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

    @FXML
    void cancelOrder(ActionEvent event) {

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

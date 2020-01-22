package org.cshaifasweng.winter;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.cshaifasweng.winter.events.CartEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class CartController implements Refreshable {


    private boolean CartIsEmpty = true;   // To setup
    private boolean LoggedIn = false;   // To setup

    public CartController() {
    }


    @Override
    public void refresh() {
    }

    @FXML
    public Button checkOutButton;

    @FXML
    private Label MainTitle;

    @FXML
    private Label SecondaryTitle;


    @FXML
    void goToCheckOut(ActionEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("order"));
    }

    @FXML
    void backToCatalog(MouseEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
    }

    @FXML
    void goToLogin(MouseEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("login_screen"));
    }

    @FXML
    private void setTitle(String title) {
        if (CartIsEmpty && LoggedIn) {
            MainTitle.setText("Your cart is empty");
        } else if (CartIsEmpty) {
            MainTitle.setText("Your cart is empty");
        } else {
            MainTitle.setText("My Cart");

        }
    }
        @Subscribe
         public void CartEventHandler(CartEvent event) {
            setTitle(event.MainTitle);
         }




}













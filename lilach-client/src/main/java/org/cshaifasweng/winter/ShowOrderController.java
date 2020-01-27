package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.events.OrderShowEvent;
import org.cshaifasweng.winter.models.Order;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.util.ResourceBundle;

public class ShowOrderController implements Refreshable, Initializable {

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

    private Stage popupStage;

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
    popupStage = event.getPopupStage();
    }

    @FXML
    void cancelOrder(ActionEvent event) {
        LilachService service = APIAccess.getService();
        service.cancelOrder(currentOrder.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Platform.runLater(() -> {
                        popupStage.close();
                        // Force refresh
                        EventBus.getDefault().post(new DashboardSwitchEvent("order_list_view"));
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {

            }
        });

    }

    @FXML
    void exitScreen(ActionEvent event) {
        popupStage.hide();
        popupStage.close();
    }

    @Override
    public void refresh() {

    }

    @Override
    public void onSwitch() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
    }
}

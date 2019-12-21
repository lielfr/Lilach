package org.cshaifasweng.winter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.cshaifasweng.winter.events.GetEvent;
import org.cshaifasweng.winter.events.SendEvent;
import org.cshaifasweng.winter.models.CatalogItem;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SecondaryController implements Initializable {

    private double currentPrice;

    private CatalogItem selectedItem = null;

    @FXML
    private TextField priceField;


    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @Subscribe
    public void handleEvent(SendEvent event) {
        selectedItem = event.message;
    }

    @FXML
    private void updatePrice() throws IOException {
        if (selectedItem != null) {
            selectedItem.setPrice(Double.parseDouble(priceField.getText()));
            APIAccess.getService().updateItem(selectedItem.getId(), selectedItem).execute();
        }
        switchToPrimary();
    }

    @FXML
    private void cancelAction() throws IOException {
        switchToPrimary();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
        currentPrice = PrimaryController.selectedItem.getPrice();
        priceField.setText(Double.toString(currentPrice));
    }
}
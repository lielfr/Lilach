package org.cshaifasweng.winter;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.events.CatalogItemEditEvent;
import org.cshaifasweng.winter.models.CatalogItem;
import org.cshaifasweng.winter.web.APIAccess;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class SecondaryController implements Refreshable {

    private double currentPrice;

    private CatalogItem selectedItem = null;

    @FXML
    private TextField priceField;


    private void switchToPrimary() throws IOException {
        EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
    }

    @Subscribe
    public void handleEvent(CatalogItemEditEvent event) {
        selectedItem = event.getCatalogItem();
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
    public void refresh() {
        EventBus.getDefault().register(this);
        currentPrice = EditCatalogListController.selectedItem.getPrice();
        priceField.setText(Double.toString(currentPrice));
    }

    @Override
    public void onSwitch() {

    }
}
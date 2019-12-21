package org.cshaifasweng.winter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class SecondaryController implements Initializable {

    private double currentPrice;

    @FXML
    private TextField priceField;


    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void updatePrice() throws IOException {
        PrimaryController.selectedItem.setPrice(Double.parseDouble(priceField.getText()));
        APIAccess.getService().updateItem(PrimaryController.selectedItem.getId(), PrimaryController.selectedItem)
                .execute();
        switchToPrimary();
    }

    @FXML
    private void cancelAction() throws IOException {
        switchToPrimary();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentPrice = PrimaryController.selectedItem.getPrice();
        priceField.setText(Double.toString(currentPrice));
    }
}
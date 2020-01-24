package org.cshaifasweng.winter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CatalogItemViewController implements Initializable {
    @FXML
    private Label itemLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public String getItemLabel() {
        return itemLabel.getText();
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel.setText(itemLabel);
    }
}

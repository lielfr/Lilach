package org.cshaifasweng.winter;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.cshaifasweng.winter.events.CatalogItemBuyEvent;
import org.cshaifasweng.winter.models.CatalogItem;
import org.greenrobot.eventbus.EventBus;

import java.net.URL;
import java.util.ResourceBundle;

public class CatalogItemViewController implements Initializable {
    @FXML
    private ImageView itemImage;

    @FXML
    private Label itemLabel;

    @FXML
    private Label itemPriceLabel;

    @FXML
    private Label itemPriceLabelDiscount;

    private PseudoClass strikethrough;

    private CatalogItem item;

    @FXML
    void buyItem() {
        EventBus.getDefault().post(new CatalogItemBuyEvent(item));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        strikethrough = PseudoClass.getPseudoClass("strikethrough");
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel.setText(itemLabel);
    }

    public ImageView getItemImage() {
        return itemImage;
    }

    public void setItemImage(Image image) {
        itemImage.setImage(image);
    }

    public void setItemPriceLabel(String itemPriceLabel) {
        this.itemPriceLabel.setText(itemPriceLabel);
    }

    public void setDiscount(double discountedPrice) {
        itemPriceLabel.pseudoClassStateChanged(strikethrough, true);
        itemPriceLabelDiscount.setVisible(true);
        itemPriceLabelDiscount.setText("Now "+ discountedPrice + " NIS");
    }

    public CatalogItem getItem() {
        return item;
    }

    public void setItem(CatalogItem item) {
        this.item = item;
    }
}

package org.cshaifasweng.winter.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.cshaifasweng.winter.events.CustomItemAddButtonEvent;
import org.cshaifasweng.winter.models.CatalogItem;
import org.greenrobot.eventbus.EventBus;

public class CustomItemCell extends ListCell<CatalogItem> {

    HBox hBox = new HBox();
    Label itemLabel = new Label("Empty");
    Pane pane = new Pane();
    Label priceLabel = new Label("0.0 NIS");
    Button addButton = new Button("+");
    public CustomItemCell() {
        super();
        hBox.getChildren().addAll(itemLabel, pane, priceLabel, addButton);
        HBox.setMargin(priceLabel, new Insets(0, 5, 0, 5));
        HBox.setHgrow(pane, Priority.ALWAYS);
        addButton.setOnAction(event -> {
            EventBus.getDefault().post(new CustomItemAddButtonEvent(getItem(), addButton.getText() == "+"));
            addButton.setText(addButton.getText() == "+" ? "-" : "+");
        });

    }

    @Override
    protected void updateItem(CatalogItem catalogItem, boolean b) {
        super.updateItem(catalogItem, b);
        setText(null);
        setGraphic(null);

        if (catalogItem != null && !b) {
            itemLabel.setText(catalogItem.getDescription());
            priceLabel.setText(catalogItem.getPrice() + " NIS");
            setGraphic(hBox);
        }
    }
}

package org.cshaifasweng.winter.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Pair;
import org.cshaifasweng.winter.events.CustomItemAddButtonEvent;
import org.cshaifasweng.winter.models.CatalogItem;
import org.greenrobot.eventbus.EventBus;

public class CustomItemCell extends ListCell<Pair<CatalogItem, Long>> {

    HBox hBox = new HBox();
    Label itemLabel = new Label("Empty");
    Pane pane = new Pane();
    Label priceLabel = new Label("0.0 NIS");
    Button addButton = new Button("+");
    Label quantityLabel = new Label("Quantity: 0");
    Button removeButton = new Button("-");
    public CustomItemCell() {
        super();
        hBox.getChildren().addAll(itemLabel, pane, priceLabel, removeButton, quantityLabel, addButton);
        HBox.setMargin(priceLabel, new Insets(0, 5, 0, 5));
        HBox.setMargin(quantityLabel, new Insets(0, 5, 0, 5));
        HBox.setHgrow(pane, Priority.ALWAYS);
        addButton.setOnAction(event -> {
            System.out.println("KEY: " + getItem().getKey());
            EventBus.getDefault().post(new CustomItemAddButtonEvent(getItem().getKey(), true));
        });

        removeButton.setOnAction(event -> {
            EventBus.getDefault().post(new CustomItemAddButtonEvent(getItem().getKey(), false));
        });

    }

    @Override
    protected void updateItem(Pair<CatalogItem, Long> pair, boolean b) {
        super.updateItem(pair, b);
        setText(null);
        setGraphic(null);
        if (pair == null || b) return;
        CatalogItem catalogItem = pair.getKey();


        if (catalogItem != null) {
            itemLabel.setText(catalogItem.getDescription());
            priceLabel.setText(catalogItem.getPrice() + " NIS");
            quantityLabel.setText("Quantity: " + pair.getValue());
            setGraphic(hBox);
        }
    }
}

package org.cshaifasweng.winter.events;

import javafx.stage.Stage;
import org.cshaifasweng.winter.models.Item;

import java.util.List;

public class PopupAddItemEvent {

    private List<Item> cart;
    Stage popupStage;

    public PopupAddItemEvent(List<Item> cart, Stage popupStage) {
        this.cart = cart;
        this.popupStage = popupStage;
    }

    public List<Item> getCart() {
        return cart;
    }

    public Stage getPopupStage() {
        return popupStage;
    }
}

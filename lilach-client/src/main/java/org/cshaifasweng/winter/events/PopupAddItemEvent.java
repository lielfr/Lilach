package org.cshaifasweng.winter.events;

import javafx.stage.Stage;
import org.cshaifasweng.winter.models.Item;

import java.util.Set;

public class PopupAddItemEvent {

    private Set<Item> cart;
    Stage popupStage;

    public PopupAddItemEvent(Set<Item> cart, Stage popupStage) {
        this.cart = cart;
        this.popupStage = popupStage;
    }

    public Set<Item> getCart() {
        return cart;
    }

    public Stage getPopupStage() {
        return popupStage;
    }
}

package org.cshaifasweng.winter.events;

import javafx.stage.Stage;
import org.cshaifasweng.winter.models.Item;

import java.util.Map;
import java.util.Set;

public class PopupAddItemEvent {

    private Set<Item> cart;
    Stage popupStage;
    private Map<Long, Long> quantities;

    public PopupAddItemEvent(Set<Item> cart, Map<Long, Long> quantities, Stage popupStage) {
        this.cart = cart;
        this.popupStage = popupStage;
        this.quantities = quantities;
    }

    public Set<Item> getCart() {
        return cart;
    }

    public Stage getPopupStage() {
        return popupStage;
    }

    public Map<Long, Long> getQuantities() {
        return quantities;
    }
}

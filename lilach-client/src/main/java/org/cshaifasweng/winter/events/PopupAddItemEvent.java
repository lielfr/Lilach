package org.cshaifasweng.winter.events;

import javafx.stage.Stage;
import org.cshaifasweng.winter.models.Item;

import java.util.List;
import java.util.Map;

public class PopupAddItemEvent {

    private List<Item> cart;
    Stage popupStage;
    private Map<Long, Long> quantities;

    public PopupAddItemEvent(List<Item> cart, Map<Long, Long> quantities, Stage popupStage) {
        this.cart = cart;
        this.popupStage = popupStage;
        this.quantities = quantities;
    }

    public List<Item> getCart() {
        return cart;
    }

    public Stage getPopupStage() {
        return popupStage;
    }

    public Map<Long, Long> getQuantities() {
        return quantities;
    }
}

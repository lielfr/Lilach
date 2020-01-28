package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.CustomItem;
import org.cshaifasweng.winter.models.Item;

import java.util.List;
import java.util.Map;

public class CustomItemFinishEvent {
    private CustomItem item;
    private List<Item> currentCart;
    private Map<Long, Long> quantities;

    public CustomItemFinishEvent(CustomItem item, List<Item> currentCart, Map<Long, Long> quantities) {
        this.item = item;
        this.currentCart = currentCart;
        this.quantities = quantities;
    }

    public CustomItem getItem() {
        return item;
    }

    public List<Item> getCurrentCart() {
        return currentCart;
    }

    public Map<Long, Long> getQuantities() {
        return quantities;
    }
}

package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.Item;

import java.util.List;
import java.util.Map;

public class BackToCatalogEvent {
    private List<Item> cart;
    private Map<Long, Long> quantities;

    public BackToCatalogEvent(List<Item> cart, Map<Long, Long> quantities) {
        this.cart = cart;
        this.quantities = quantities;
    }

    public List<Item> getCart() {
        return cart;
    }

    public Map<Long, Long> getQuantities() {
        return quantities;
    }
}

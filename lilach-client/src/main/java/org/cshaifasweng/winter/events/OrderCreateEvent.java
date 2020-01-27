package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.Item;

import java.util.*;

public class OrderCreateEvent {
    private List<Item> cart;
    private Map<Long, Long> quantities;

    public OrderCreateEvent(List<Item> cart, Map<Long, Long> quantities) {
        this.cart = new ArrayList<>(cart);
        this.quantities = quantities;
    }

    public List<Item> getCart() {
        return cart;
    }

    public Map<Long, Long> getQuantities() {
        return quantities;
    }
}

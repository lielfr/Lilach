package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.Item;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OrderCreateEvent {
    private Set<Item> cart;
    private Map<Long, Long> quantities;

    public OrderCreateEvent(Set<Item> cart, Map<Long, Long> quantities) {
        this.cart = new HashSet<>(cart);
        this.quantities = quantities;
    }

    public Set<Item> getCart() {
        return cart;
    }

    public Map<Long, Long> getQuantities() {
        return quantities;
    }
}

package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.Item;

import java.util.HashSet;
import java.util.Set;

public class OrderCreateEvent {
    private Set<Item> cart;

    public OrderCreateEvent(Set<Item> cart) {
        this.cart = new HashSet<>(cart);
    }

    public Set<Item> getCart() {
        return cart;
    }
}

package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.Item;

import java.util.ArrayList;
import java.util.List;

public class OrderCreateEvent {
    private List<Item> cart;

    public OrderCreateEvent(List<Item> cart) {
        this.cart = new ArrayList<>(cart);
    }

    public List<Item> getCart() {
        return cart;
    }
}

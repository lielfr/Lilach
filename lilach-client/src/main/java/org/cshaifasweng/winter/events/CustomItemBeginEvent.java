package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.Item;
import org.cshaifasweng.winter.models.Store;

import java.util.List;
import java.util.Map;

public class CustomItemBeginEvent {
    private Store store;
    private List<Item> currentCart;
    private Map<Long, Long> quantities;

    public CustomItemBeginEvent(Store store, List<Item> currentCart, Map<Long, Long> quantities) {
        this.store = store;
        this.currentCart = currentCart;
        this.quantities = quantities;
    }

    public Store getStore() {
        return store;
    }

    public List<Item> getCurrentCart() {
        return currentCart;
    }

    public Map<Long, Long> getQuantities() {
        return quantities;
    }
}

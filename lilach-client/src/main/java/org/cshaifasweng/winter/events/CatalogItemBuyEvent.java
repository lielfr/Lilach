package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.CatalogItem;

public class CatalogItemBuyEvent {
    private CatalogItem item;
    private int quantity;

    public CatalogItemBuyEvent(CatalogItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public CatalogItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }
}

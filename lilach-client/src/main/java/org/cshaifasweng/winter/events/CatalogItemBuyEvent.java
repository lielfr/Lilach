package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.CatalogItem;

public class CatalogItemBuyEvent {
    private CatalogItem item;

    public CatalogItemBuyEvent(CatalogItem item) {
        this.item = item;
    }

    public CatalogItem getItem() {
        return item;
    }
}

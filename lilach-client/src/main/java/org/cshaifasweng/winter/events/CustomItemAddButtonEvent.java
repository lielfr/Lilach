package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.CatalogItem;

public class CustomItemAddButtonEvent {
    private CatalogItem item;
    private boolean isAdd;

    public CustomItemAddButtonEvent(CatalogItem item, boolean isAdd) {
        this.item = item;
        this.isAdd = isAdd;
    }

    public CatalogItem getItem() {
        return item;
    }

    public boolean isAdd() {
        return isAdd;
    }
}

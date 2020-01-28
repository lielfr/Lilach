package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.CatalogItem;

public class CatalogItemEditEvent{
    private CatalogItem catalogItem;

    public CatalogItemEditEvent(CatalogItem catalogItem) {
        this.catalogItem = catalogItem;
    }

    public CatalogItem getCatalogItem() {
        return catalogItem;
    }
}

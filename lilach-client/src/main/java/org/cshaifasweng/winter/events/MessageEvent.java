package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.CatalogItem;

public class MessageEvent {
    public final CatalogItem message;

    public MessageEvent(CatalogItem message) {
        this.message = message;
    }
}

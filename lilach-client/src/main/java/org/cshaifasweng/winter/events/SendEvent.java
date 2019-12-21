package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.CatalogItem;

public class SendEvent extends MessageEvent {
    public SendEvent(CatalogItem message) {
        super(message);
    }
}

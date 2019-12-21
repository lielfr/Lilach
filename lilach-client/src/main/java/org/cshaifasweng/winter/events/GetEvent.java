package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.CatalogItem;

public class GetEvent extends MessageEvent {
    public GetEvent(CatalogItem message) {
        super(message);
    }
}

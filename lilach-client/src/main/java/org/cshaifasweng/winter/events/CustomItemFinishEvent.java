package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.CustomItem;

public class CustomItemFinishEvent {
    private CustomItem item;

    public CustomItemFinishEvent(CustomItem item) {
        this.item = item;
    }

    public CustomItem getItem() {
        return item;
    }
}

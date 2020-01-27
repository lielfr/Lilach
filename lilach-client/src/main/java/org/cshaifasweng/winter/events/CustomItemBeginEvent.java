package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.Store;

public class CustomItemBeginEvent {
    private Store store;

    public CustomItemBeginEvent(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
    }
}

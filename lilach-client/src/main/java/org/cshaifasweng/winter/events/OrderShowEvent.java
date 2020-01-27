package org.cshaifasweng.winter.events;

import org.cshaifasweng.winter.models.Order;

public class OrderShowEvent {
    private Order order;

    public OrderShowEvent(Order order){
        this.order = order;
    }

    public Order getOrder(){
        return order;
    }
}

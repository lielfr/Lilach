package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.HashMap;
import java.util.Map;

@JsonTypeName("orders_report")
public class OrdersReport extends Report {
    private long ordersAmount;

    private Map<CatalogItemType, Long> ordersByType;

    public OrdersReport() {
        super();
        ordersAmount = 0;
        ordersByType = new HashMap<>();
    }

    public long getOrdersAmount() {
        return ordersAmount;
    }

    public void setOrdersAmount(long ordersAmount) {
        this.ordersAmount = ordersAmount;
    }

    public Map<CatalogItemType, Long> getOrdersByType() {
        return ordersByType;
    }

    public void setOrdersByType(Map<CatalogItemType, Long> ordersByType) {
        this.ordersByType = ordersByType;
    }
}

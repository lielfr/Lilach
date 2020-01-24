package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@JsonTypeName("orders_report")
public class OrdersReport extends Report {
    private long ordersAmount;

    @ElementCollection
    private Map<CatalogItemType, String> ordersByType;

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

    public Map<CatalogItemType, String> getOrdersByType() {
        return ordersByType;
    }

    public void setOrdersByType(Map<CatalogItemType, String> ordersByType) {
        this.ordersByType = ordersByType;
    }
}

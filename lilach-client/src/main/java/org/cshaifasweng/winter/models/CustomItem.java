package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonTypeName("custom_item")
public class CustomItem extends Item {
    private List<CatalogItem> items;
    private Map<Long, Long> quantities;

    private double lowerPriceBound;
    private double upperPriceBound;

    private String dominantColor;

    private CustomItemType type;

    public CustomItem() {
        super();
        items = new ArrayList<>();
        quantities = new HashMap<>();
    }

    public CustomItem(double lowerPriceBound, double upperPriceBound, CustomItemType type) {
        super();
        items = new ArrayList<>();
        this.lowerPriceBound = lowerPriceBound;
        this.upperPriceBound = upperPriceBound;
        this.type = type;
        quantities = new HashMap<>();
    }

    public List<CatalogItem> getItems() {
        return items;
    }

    public void setItems(List<CatalogItem> items) {
        this.items = items;
    }

    public double getLowerPriceBound() {
        return lowerPriceBound;
    }

    public void setLowerPriceBound(double lowerPriceBound) {
        this.lowerPriceBound = lowerPriceBound;
    }

    public double getUpperPriceBound() {
        return upperPriceBound;
    }

    public void setUpperPriceBound(double upperPriceBound) {
        this.upperPriceBound = upperPriceBound;
    }

    public String getDominantColor() {
        return dominantColor;
    }

    public void setDominantColor(String dominantColor) {
        this.dominantColor = dominantColor;
    }

    public CustomItemType getType() {
        return type;
    }

    public void setType(CustomItemType type) {
        this.type = type;
    }

    public Map<Long, Long> getQuantities() {
        return quantities;
    }

    public void setQuantities(Map<Long, Long> quantities) {
        this.quantities = quantities;
    }
}

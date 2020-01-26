package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName("catalog_item")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class CatalogItem extends Item {

    public CatalogItem() {
        super();
    }

    private String description;

    private String picture;

    private long availableCount = 0;

    private long itemsSold = 0;

    @JsonIgnore
    private Store store;

    @JsonIgnore
    private List<CustomItem> customItems;

    private boolean canBeAssembled;

    private double discountAmount;

    private double discountPercent;

    private CatalogItemType itemType;

    public CatalogItem(double price, String description,
                       String picture, long availableCount, Store store, boolean canBeAssembled,
                       CatalogItemType itemType) {
        this.price = price;
        this.description = description;
        this.picture = picture;
        this.availableCount = availableCount;
        this.store = store;
        this.customItems = new ArrayList<>();
        this.canBeAssembled = canBeAssembled;
        this.discountAmount = 0.0;
        this.discountPercent = 0.0;
        this.itemType = itemType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public long getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(long availableCount) {
        this.availableCount = availableCount;
    }

    public long getItemsSold() {
        return itemsSold;
    }

    public void setItemsSold(long itemsSold) {
        this.itemsSold = itemsSold;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<CustomItem> getCustomItems() {
        return customItems;
    }

    public void setCustomItems(List<CustomItem> customItems) {
        this.customItems = customItems;
    }

    public boolean isCanBeAssembled() {
        return canBeAssembled;
    }

    public void setCanBeAssembled(boolean canBeAssembled) {
        this.canBeAssembled = canBeAssembled;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public CatalogItemType getItemType() {
        return itemType;
    }

    public void setItemType(CatalogItemType itemType) {
        this.itemType = itemType;
    }
}

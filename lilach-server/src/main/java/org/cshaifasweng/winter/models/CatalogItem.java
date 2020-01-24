package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@JsonTypeName("catalog_item")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class CatalogItem extends Item {

    public CatalogItem() {
        super();
    }

    private String description;

    private String dominantColor;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] picture;

    private long availableCount = 0;

    private long itemsSold = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Store store;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CustomItem> customItems;

    private boolean canBeAssembled;

    public CatalogItem(double price, String description, String dominantColor,
                       byte[] picture, long availableCount, Store store, boolean canBeAssembled) {
        this.price = price;
        this.description = description;
        this.dominantColor = dominantColor;
        this.picture = picture.clone();
        this.availableCount = availableCount;
        this.store = store;
        this.customItems = new ArrayList<>();
        this.canBeAssembled = canBeAssembled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDominantColor() {
        return dominantColor;
    }

    public void setDominantColor(String dominantColor) {
        this.dominantColor = dominantColor;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
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
}

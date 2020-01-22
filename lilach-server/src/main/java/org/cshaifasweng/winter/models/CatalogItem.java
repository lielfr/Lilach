package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class CatalogItem {

    public CatalogItem() {
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    private double price;

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

    public CatalogItem(double price, String description, String dominantColor,
                       byte[] picture, long availableCount, Store store) {
        this.price = price;
        this.description = description;
        this.dominantColor = dominantColor;
        this.picture = picture.clone();
        this.availableCount = availableCount;
        this.store = store;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
}

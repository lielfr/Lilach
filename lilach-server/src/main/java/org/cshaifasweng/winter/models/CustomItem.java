package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonTypeName("custom_item")
public class CustomItem extends Item {

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "custom_items_items",
    joinColumns = @JoinColumn(name = "custom_item_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id"))
    private List<CatalogItem> items;

    private double totalSum;

    public CustomItem() {
        super();
        items = new ArrayList<>();
    }

    public List<CatalogItem> getItems() {
        return items;
    }

    public void setItems(List<CatalogItem> items) {
        this.items = items;
    }
}

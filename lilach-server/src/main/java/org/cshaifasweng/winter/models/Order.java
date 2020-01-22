package org.cshaifasweng.winter.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private DeliveryMethod deliveryMethod;

    private Date orderDate;

    private Date supplyDate;

    private String greeting;

    private boolean deliverToAnother;

    private String deliveryAddress;

    private String recipientMail;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Customer orderedBy;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Store store;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CatalogItem> items;

    private double price;

    public Order() {
        this.items = new ArrayList<>();
    }

    public Order(Date supplyDate, Store store, Customer orderedBy, DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
        this.supplyDate = supplyDate;
        this.store = store;
        this.orderedBy = orderedBy;
        this.items = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public Date getSupplyDate() {
        return supplyDate;
    }

    public void setSupplyDate(Date supplyDate) {
        this.supplyDate = supplyDate;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public boolean isDeliverToAnother() {
        return deliverToAnother;
    }

    public void setDeliverToAnother(boolean deliverToAnother) {
        this.deliverToAnother = deliverToAnother;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getRecipientMail() {
        return recipientMail;
    }

    public void setRecipientMail(String recipientMail) {
        this.recipientMail = recipientMail;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<CatalogItem> getItems() {
        return items;
    }

    public void setItems(List<CatalogItem> items) {
        this.items = items;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Customer getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(Customer orderedBy) {
        this.orderedBy = orderedBy;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}





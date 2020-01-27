package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private boolean purchased;
    private String description;
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean ordered;

    // TODO: Change this once we have an order entity in place.
    private String orderNum;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean email;

    private String answer;

    private Double compensation = 0.0;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isOpen;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Customer openedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    private Store store;

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    private Employee handledBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date complaintOpen;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date complaintClose;

    public Complaint() {
    }

    public Complaint(String description, boolean email, Customer openedBy) {
        this.description = description;
        this.email = email;
        this.openedBy = openedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Double getCompensation() {
        return compensation;
    }

    public void setCompensation(Double compensation) {
        this.compensation = compensation;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Date getComplaintOpen() {
        return complaintOpen;
    }

    public void setComplaintOpen(Date complaintOpen) {
        this.complaintOpen = complaintOpen;
    }

    public Date getComplaintClose() {
        return complaintClose;
    }

    public void setComplaintClose(Date complaintClose) {
        this.complaintClose = complaintClose;
    }

    public Customer getOpenedBy() {
        return openedBy;
    }

    public void setOpenedBy(Customer openedBy) {
        this.openedBy = openedBy;
    }

    public Employee getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(Employee handledBy) {
        this.handledBy = handledBy;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}

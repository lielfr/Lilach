package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Complaint {
    private Long id;

//    private boolean purchased;
    private String description;
    private boolean ordered;

    // TODO: Change this once we have an order entity in place.
    private String orderNum;

    private boolean email;

    private String answer;

    private Double compensation = 0.0;

    private  boolean isOpen;

    private Customer openedBy;

    private Store store;

    private Employee handledBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date complaintOpen;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date complaintClose;

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

    private void getOpenTime(){
        complaintOpen = new Date();
        String StrDateFormat =  "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(StrDateFormat);
        String formattedDate= dateFormat.format(complaintOpen);

    }
    private void getCloseTime(){
        complaintClose = new Date();
        String StrDateFormat =  "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(StrDateFormat);
        String formattedDate= dateFormat.format(complaintClose);

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

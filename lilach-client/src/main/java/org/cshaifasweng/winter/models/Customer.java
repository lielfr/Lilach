package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@JsonTypeName("customer")
public class Customer extends User {

    private String address;

    private Long creditCard;

    private Date expireDate;

    private Integer cvv;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateOfBirth;

    private SubscriberType subscriberType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date subscriptionEnd;

    private List<Transaction> transactions;

    private Collection<Complaint> complaints;


    public Customer(String email, String password, String firstName, String lastName, String phone, long creditCard, Date expireDate, int cvv, Date dateOfBirth) {
        super(email, password, firstName, lastName, phone);
        this.creditCard = creditCard;
        this.dateOfBirth = dateOfBirth;
        this.expireDate = expireDate;
        this.cvv = cvv;
        this.transactions = new ArrayList<>();
    }

    public Customer() {
    }

    @Override
    public User copy() {
        Customer copy = new Customer(email, password, firstName, lastName, phone,
                creditCard, expireDate, cvv, (Date)dateOfBirth.clone());
        return copy;
    }

    public long getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(long creditCard) {
        this.creditCard = creditCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCreditCard(Long creditCard) {
        this.creditCard = creditCard;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public SubscriberType getSubscriberType() {
        return subscriberType;
    }

    public void setSubscriberType(SubscriberType subscriberType) {
        this.subscriberType = subscriberType;
    }

    public Date getSubscriptionEnd() {
        return subscriptionEnd;
    }

    public void setSubscriptionEnd(Date subscriptionEnd) {
        this.subscriptionEnd = subscriptionEnd;
    }

    public Collection<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(Collection<Complaint> complaints) {
        this.complaints = complaints;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
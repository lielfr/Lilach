package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@JsonTypeName("employee")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Employee extends User {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date employedSince;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "manager")
    @JsonIgnore
    private Store managedStore;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Store assignedStore;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "reportsTo")
    @JsonIgnore
    private Collection<Employee> subordinates;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Employee reportsTo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "handledBy")
    @JsonIgnore
    private Collection<Complaint> handledComplaints;

    public Employee() {}

    @Override
    public User copy() {
        Employee copy = new Employee(misparZehut, email, password, firstName, lastName, phone, roles, (Date) employedSince.clone());
        if (managedStore != null)
            copy.setManagedStore(new Store(managedStore));
        if (reportsTo != null)
            copy.setReportsTo((Employee) reportsTo.copy());
        copy.setHandledComplaints(handledComplaints);
        if (assignedStore != null)
            copy.setAssignedStore(new Store(assignedStore));
        return copy;
    }

    public Employee(String misparZehut, String email, String password, String firstName, String lastName,
                    String phone, Collection<Role> roles, Date employedSince) {
        super(misparZehut, email, password, firstName, lastName, phone, roles);
        this.employedSince = employedSince;
    }

    public Date getEmployedSince() {
        return employedSince;
    }

    public void setEmployedSince(Date employedSince) {
        this.employedSince = employedSince;
    }

    public Collection<Complaint> getHandledComplaints() {
        return handledComplaints;
    }

    public void setHandledComplaints(Collection<Complaint> handledComplaints) {
        this.handledComplaints = handledComplaints;
    }

    public Store getManagedStore() {
        return managedStore;
    }

    public void setManagedStore(Store managedStore) {
        this.managedStore = managedStore;
    }

    public Collection<Employee> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(Collection<Employee> subordinates) {
        this.subordinates = subordinates;
    }

    public Employee getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(Employee reportsTo) {
        this.reportsTo = reportsTo;
    }

    public Store getAssignedStore() {
        return assignedStore;
    }

    public void setAssignedStore(Store assignedStore) {
        this.assignedStore = assignedStore;
    }
}

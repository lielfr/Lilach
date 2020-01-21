package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.Date;

@Entity
@JsonTypeName("employee")
public class Employee extends User {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date employedSince;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "handledBy")
    private Collection<Complaint> handledComplaints;

    public Employee() {}

    @Override
    public User copy() {
        Employee copy = new Employee(email, password, firstName, lastName, phone, (Date) employedSince.clone());
        return copy;
    }

    public Employee(String email, String password, String firstName, String lastName,
                    String phone, Date employedSince) {
        super(email, password, firstName, lastName, phone);
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
}

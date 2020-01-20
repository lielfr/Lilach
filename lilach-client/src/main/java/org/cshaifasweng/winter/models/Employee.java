package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Collection;
import java.util.Date;

@JsonTypeName("employee")
public class Employee extends User {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date employedSince;

    public Employee() {}

    @Override
    public User copy() {
        Employee copy = new Employee(email, password, firstName, lastName, phone, roles, (Date) employedSince.clone());
        return copy;
    }

    public Employee(String email, String password, String firstName, String lastName,
                    String phone, Collection<Role> roles, Date employedSince) {
        super(email, password, firstName, lastName, phone, roles);
        this.employedSince = employedSince;
    }

    public Date getEmployedSince() {
        return employedSince;
    }

    public void setEmployedSince(Date employedSince) {
        this.employedSince = employedSince;
    }
}

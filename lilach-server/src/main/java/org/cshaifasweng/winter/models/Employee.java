package org.cshaifasweng.winter.models;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;

@Entity
public class Employee extends User {

    public Employee() {}

    public Employee(String email, String password, String firstName, String lastName,
                    String phone, Collection<Role> roles) {
        super(email, password, firstName, lastName, phone, roles);
    }
}

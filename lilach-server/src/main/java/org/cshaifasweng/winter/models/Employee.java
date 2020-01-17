package org.cshaifasweng.winter.models;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;

@Entity
public class Employee extends User {

    public Employee() {}

    public Employee(String email, String password, Collection<Role> roles) {
        super(email, password, roles);
    }
}

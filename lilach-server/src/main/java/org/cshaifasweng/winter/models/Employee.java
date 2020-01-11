package org.cshaifasweng.winter.models;

import javax.persistence.Entity;
import java.util.Collection;

@Entity
public class Employee extends User {

    public Employee() {}

    public Employee(String email, String password) {
        super(email, password);
    }
}

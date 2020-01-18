package org.cshaifasweng.winter.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;

public class Role {
    private Long id;

    private String name;

    private Collection<Privilege> privileges;

    public Role() {
    }

    public Role(String name, Collection<Privilege> privileges) {
        this.name = name;
        this.privileges = privileges;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Collection<Privilege> privileges) {
        this.privileges = privileges;
    }
}

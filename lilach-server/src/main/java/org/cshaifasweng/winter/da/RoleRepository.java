package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role findByName(String name);
}

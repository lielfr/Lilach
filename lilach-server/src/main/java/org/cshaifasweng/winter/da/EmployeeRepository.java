package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.Employee;
import org.cshaifasweng.winter.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    public Employee findByEmail(String email);

    public List<Employee> findAllByRolesContaining(Role role);
}

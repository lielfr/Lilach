package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    public Employee findByEmail(String email);
}

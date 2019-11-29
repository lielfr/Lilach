package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

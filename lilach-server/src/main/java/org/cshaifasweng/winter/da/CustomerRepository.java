package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    public Customer findByEmail(String email);

    public List<Customer> findAllByStoresContains(Store store);
}

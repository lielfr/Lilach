package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByName(String name);

    List<Store> findAllByCustomersContains(Customer customer);
}

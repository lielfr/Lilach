package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Order;
import org.cshaifasweng.winter.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStore(Store store);
    List<Order> findAllByOrderedBy(Customer customer);
}

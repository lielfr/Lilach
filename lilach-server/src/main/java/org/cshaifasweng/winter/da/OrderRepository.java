package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

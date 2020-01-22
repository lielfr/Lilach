package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.Order;
import org.cshaifasweng.winter.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public Order newOrder(@RequestBody Order order) throws LogicalException {
        return orderService.newOrder(order);
    }
}

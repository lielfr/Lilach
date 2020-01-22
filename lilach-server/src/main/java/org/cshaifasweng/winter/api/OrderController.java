package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Order;
import org.cshaifasweng.winter.models.OrderCompensation;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.services.OrderService;
import org.cshaifasweng.winter.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;


    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/order")
    public Order newOrder(@RequestBody Order order, Authentication authentication) throws LogicalException {
        User user = userService.getUser(authentication.getName());

        if (!(user instanceof Customer))
            throw new LogicalException("Only customers can order stuff");

        return orderService.newOrder(order, (Customer)user);
    }

    @DeleteMapping("/order/{id}")
    public OrderCompensation cancelOrder(@PathVariable("id") long id, Authentication authentication) throws LogicalException {
        User user = userService.getUser(authentication.getName());

        if (user instanceof Customer)
            return orderService.cancelOrderCustomer(id, (Customer) user);

        // TODO: Allow employees with the required permission to cancel every order without all the logic.
        return null;
    }
}

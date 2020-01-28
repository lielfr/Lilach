package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.da.PrivilegeRepository;
import org.cshaifasweng.winter.da.RoleRepository;
import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Order;
import org.cshaifasweng.winter.models.OrderCompensation;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.security.SecurityConstants;
import org.cshaifasweng.winter.services.OrderService;
import org.cshaifasweng.winter.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;


    public OrderController(OrderService orderService, UserService userService, RoleRepository roleRepository, PrivilegeRepository repository) {
        this.orderService = orderService;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.privilegeRepository = repository;
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

        if (user.getRoles().stream().noneMatch(role -> role.getPrivileges().contains(
                privilegeRepository.findByName(SecurityConstants.PRIVILEGE_ORDERS_CANCEL_ALL)))) {
            throw new LogicalException("You don't have permission to cancel an order");
        }

        return orderService.cancelOrderEmployee(id);
    }

    @PutMapping("/order/{id}/delivered")
    public Order markOrderAsDelivered(@PathVariable("id") long id) {
        return orderService.markAsDelivered(id);
    }
}

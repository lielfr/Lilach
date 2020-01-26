package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.da.PrivilegeRepository;
import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Order;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.security.SecurityConstants;
import org.cshaifasweng.winter.services.OrderService;
import org.cshaifasweng.winter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    private final UserService userService;
    private final PrivilegeRepository privilegeRepository;
    private final OrderService orderService;

    @Autowired
    public CustomerController(UserService userService, PrivilegeRepository privilegeRepository, OrderService orderService) {
        this.userService = userService;
        this.privilegeRepository = privilegeRepository;
        this.orderService = orderService;
    }

    @PostMapping("/customer")
    public Customer newCustomer(@RequestBody Customer customer) throws LogicalException {
        return userService.newCustomer(customer);
    }

    @PutMapping("/customer/{id}")
    public Customer updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer,
                                   Authentication authentication) throws LogicalException {
        User user = userService.getUser(authentication.getName());

        if (user.getId().equals(id))
            return userService.updateCustomer(id, customer);

        if (user.getRoles().stream().noneMatch(
                (predicate) -> predicate.getPrivileges().
                        contains(privilegeRepository.findByName(SecurityConstants.PRIVILEGE_USERS_EDIT)))) {
            throw new LogicalException("Not authorized to edit users");
        }

        return userService.updateCustomer(id, customer);
    }

    @GetMapping("/customer/{id}/orders")
    public List<Order> getOrdersByCustomer(@PathVariable("id") long id,
                                           Authentication authentication) throws LogicalException {
        return orderService.findByCustomer(id, authentication);
    }
}

package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.da.PrivilegeRepository;
import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.*;
import org.cshaifasweng.winter.security.SecurityConstants;
import org.cshaifasweng.winter.services.ComplaintService;
import org.cshaifasweng.winter.services.OrderService;
import org.cshaifasweng.winter.services.StoreService;
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
    private final ComplaintService complaintService;
    private final StoreService storeService;

    @Autowired
    public CustomerController(UserService userService, PrivilegeRepository privilegeRepository, OrderService orderService, ComplaintService complaintService, StoreService storeService) {
        this.userService = userService;
        this.privilegeRepository = privilegeRepository;
        this.orderService = orderService;
        this.complaintService = complaintService;
        this.storeService = storeService;
    }

    @PostMapping("/customer")
    public Customer newCustomer(@RequestBody Customer customer, @RequestParam("store") long storeId) throws LogicalException {
        return userService.newCustomer(customer, storeId);
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

    @GetMapping("/customer/{id}/complaints")
    public List<Complaint> getComplaintsByCustomer(@PathVariable("id") long id,
                                                   Authentication authentication) throws LogicalException {
        return complaintService.getComplaintsByCustomer(id, authentication);
    }

    @GetMapping("/customer/{id}/stores")
    public List<Store> getStoresByCustomer(@PathVariable("id") long id, Authentication authentication) throws LogicalException {
        return storeService.getStoresByCustomer(id, authentication);
    }
}

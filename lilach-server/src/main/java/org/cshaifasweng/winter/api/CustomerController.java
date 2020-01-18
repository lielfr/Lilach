package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
public class CustomerController {

    private final UserService userService;

    @Autowired
    public CustomerController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/customer")
    public Customer newCustomer(@RequestBody Customer customer) throws LogicalException {
        return userService.newCustomer(customer);
    }
}

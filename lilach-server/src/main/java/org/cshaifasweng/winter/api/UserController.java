package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.models.UserType;
import org.cshaifasweng.winter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<User> getUsersList(@RequestParam(value = "store", required = false) Optional<Long> storeId,
                                   @RequestParam("type") UserType userType) {
        if (storeId.isPresent()) {
            switch (userType) {
                case EMPLOYEE:
                    return userService.getEmployeesByStore(storeId.get());
                case CUSTOMER:
                    return userService.getCustomersByStore(storeId.get());
                case ALL:
                    return userService.getUsersByStore(storeId.get());
            }
        } else {
            switch (userType) {
                case EMPLOYEE:
                    return userService.getAllEmployees();
                case CUSTOMER:
                    return userService.getAllCustomers();
                case ALL:
                    return userService.getAllUsers();
            }
        }

        // Should never be reached, but just in case.
        return null;
    }

    @GetMapping("/user/me")
    public User getCurrentUser(Authentication authentication) throws LogicalException {
        if (authentication == null)
            throw new LogicalException("Authentication does not exist");

        return userService.getUser(authentication.getName());
    }


}

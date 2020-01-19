package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/me")
    public User getCurrentUser(Authentication authentication) throws LogicalException {
        if (authentication == null)
            throw new LogicalException("Authentication does not exist");

        return userService.getUser(authentication.getName());
    }
}

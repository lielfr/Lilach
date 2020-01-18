package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.UserRepository;
import org.cshaifasweng.winter.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void setUserLoggedIn(long id, boolean isLoggedIn) {
        User user = userRepository.getOne(id);
        user.setLoggedIn(isLoggedIn);
        userRepository.save(user);
    }

    @Transactional
    public void setUserLoggedIn(String email, boolean isLoggedIn) {
        User user = userRepository.findByEmail(email);
        user.setLoggedIn(isLoggedIn);
        userRepository.save(user);
    }
}

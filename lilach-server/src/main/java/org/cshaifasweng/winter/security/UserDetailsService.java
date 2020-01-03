package org.cshaifasweng.winter.security;

import org.cshaifasweng.winter.da.UserRepository;
import org.cshaifasweng.winter.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(s);

        if (user == null) {
            throw new UsernameNotFoundException(s);
        }
        return new UserPrincipal(user);
    }
}

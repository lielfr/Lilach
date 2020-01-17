package org.cshaifasweng.winter.security;

import org.cshaifasweng.winter.da.UserRepository;
import org.cshaifasweng.winter.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LilachLogoutHandler implements LogoutSuccessHandler {

    private final UserRepository userRepository;

    public LilachLogoutHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String tokenHeader = httpServletRequest.getHeader("Authorization");
        if (tokenHeader == null)
            return;
        String token = tokenHeader
                .replace(SecurityConstants.TOKEN_PREFIX, "");

        User user = userRepository.findByLoginToken(token);
        if (user != null) {
            user.setLoginToken(null);
            userRepository.save(user);
        }
    }
}

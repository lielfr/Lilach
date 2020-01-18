package org.cshaifasweng.winter.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.cshaifasweng.winter.da.UserRepository;
import org.cshaifasweng.winter.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LilachLogoutHandler implements LogoutSuccessHandler {

    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(LilachLogoutHandler.class);

    @Autowired
    public LilachLogoutHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String tokenHeader = httpServletRequest.getHeader("Authorization");
        log.debug("Got tokenHeader: " + tokenHeader);
        if (tokenHeader == null)
            return;
        String token = tokenHeader
                .replace(SecurityConstants.TOKEN_PREFIX, "");


        if (!StringUtils.isEmpty(token)) {
            try {
                byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

                Jws<Claims> parsedToken = Jwts.parser().setSigningKey(signingKey)
                        .parseClaimsJws(token);
                String email = parsedToken.getBody().getSubject();
                log.debug("Got email:" + email);

                if (!StringUtils.isEmpty(email)) {
                    userService.setUserLoggedIn(email, false);
                    log.debug("User {} is logged out successfully and can log in again.", email);
                }

            } catch (ExpiredJwtException e) {
                log.warn("Request to parse expired JWT {} failed: {}", token, e.getMessage());
            } catch (UnsupportedJwtException e) {
                log.warn("Request to parse unsupported JWT {} failed: {}", token, e.getMessage());
            } catch (MalformedJwtException e) {
                log.warn("Request to parse malformed JWT {} failed: {}", token, e.getMessage());
            } catch (SignatureException e) {
                log.warn("Request to parse JWT {} with invalid signature failed: {}", token, e.getMessage());
            } catch (IllegalArgumentException e) {
                log.warn("Request to parse empty or null JWT {} failed: {}", token, e.getMessage());
            }
        }
    }
}

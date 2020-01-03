package org.cshaifasweng.winter.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationFilter.class);

    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken token = getAuthentication(request);

        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(token);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);

        if (!StringUtils.isEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            try {
                byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

                Jws<Claims> parsedToken = Jwts.parser().setSigningKey(signingKey)
                        .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""));
                String username = parsedToken.getBody().getSubject();

                List<SimpleGrantedAuthority> authorities = ((List<?>) parsedToken.getBody()
                        .get("rol")).stream()
                        .map(authority -> new SimpleGrantedAuthority((String) authority))
                        .collect(Collectors.toList());

                if (!StringUtils.isEmpty(username)) {
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
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

        return null;
    }
}

package org.cshaifasweng.winter.security;


import org.springframework.security.core.AuthenticationException;

public class AlreadyLoggedInException extends AuthenticationException {

    public AlreadyLoggedInException(String message) {
        super(message);
    }
}

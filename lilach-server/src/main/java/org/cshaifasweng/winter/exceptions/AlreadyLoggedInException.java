package org.cshaifasweng.winter.exceptions;


import org.springframework.security.core.AuthenticationException;

public class AlreadyLoggedInException extends AuthenticationException {

    public AlreadyLoggedInException(String message) {
        super(message);
    }
}

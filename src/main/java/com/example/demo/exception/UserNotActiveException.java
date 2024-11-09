package com.example.demo.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotActiveException extends AuthenticationException {
    public UserNotActiveException(String message) {
        super(message);
    }
}
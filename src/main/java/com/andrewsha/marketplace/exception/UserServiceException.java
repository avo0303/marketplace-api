package com.andrewsha.marketplace.exception;

public class UserServiceException extends RuntimeException {

    private static final long serialVersionUID = -6076361023694209844L;

    public UserServiceException(String message) {
        super(message);
    }
}

package com.andrewsha.marketplace.exception;

public class StoreServiceException extends RuntimeException {
    private static final long serialVersionUID = -4700656969213061131L;

    public StoreServiceException(String message) {
        super(message);
    }
}

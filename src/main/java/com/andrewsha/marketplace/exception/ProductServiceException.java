package com.andrewsha.marketplace.exception;

public class ProductServiceException extends RuntimeException {

    private static final long serialVersionUID = -8909580580868688093L;

    public ProductServiceException(String message) {
        super(message);
    }
}

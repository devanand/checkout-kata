package com.haiilo.checkout.exception;

public class UnknownProductException extends RuntimeException {

    public static final String ERROR_CODE = "UNKNOWN_PRODUCT";
    private static final String MESSAGE_TEMPLATE = "Unknown product: %s";

    public UnknownProductException(String productId) {
        super(MESSAGE_TEMPLATE.formatted(productId));
    }
}

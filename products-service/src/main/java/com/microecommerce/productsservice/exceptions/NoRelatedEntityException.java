package com.microecommerce.productsservice.exceptions;

public class NoRelatedEntityException extends Exception {
    public NoRelatedEntityException(String message) {
        super(message);
    }
}

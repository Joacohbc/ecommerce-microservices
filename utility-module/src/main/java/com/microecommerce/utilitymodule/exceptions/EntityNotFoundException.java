package com.microecommerce.utilitymodule.exceptions;

// Use: When the main entity in the operation is not found
public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String message) {
        super(message);
    }
}

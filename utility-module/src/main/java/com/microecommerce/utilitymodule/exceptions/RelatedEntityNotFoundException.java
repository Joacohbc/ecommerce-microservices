package com.microecommerce.utilitymodule.exceptions;

// Use: When one or more no-main entity (related to the main entity) in the operation is not found
public class RelatedEntityNotFoundException extends Exception {
    public RelatedEntityNotFoundException(String message) {
        super(message);
    }
}

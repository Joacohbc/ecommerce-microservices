package com.microecommerce.utilitymodule.exceptions;

// Use: When a relation is duplicated (e.g. a user is already a member of a group)
public class DuplicatedRelationException extends Exception {
    public DuplicatedRelationException(String message) {
        super(message);
    }
}

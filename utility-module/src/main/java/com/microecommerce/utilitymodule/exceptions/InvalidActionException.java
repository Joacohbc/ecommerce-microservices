package com.microecommerce.utilitymodule.exceptions;

// Use: When an invalid action is attempted
public class InvalidActionException extends Exception {
    public InvalidActionException(String message) {
        super(message);
    }
}

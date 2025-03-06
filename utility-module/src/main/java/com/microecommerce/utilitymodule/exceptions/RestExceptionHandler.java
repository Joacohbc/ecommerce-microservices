package com.microecommerce.utilitymodule.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

// Handles exceptions and returns a response with the appropriate status code and message
// Exception:
// - DuplicatedRelationException (Custom) - 400
// - InvalidEntityException (Custom) - 400
// - EntityNotFoundException (Custom) - 404
// - RelatedEntityNotFoundException (Custom) - 400
// - InvalidActionException (Custom) - 400
// - ConstraintViolationException (Validation Errors) - 400
// - ResponseStatusException (Generic)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    public static Map<String, Object> createJsonResponse(String message, Object data, HttpStatus statusCode) {
        Map<String, Object> json = new HashMap<>();
        json.put("message", message);
        json.put("data", data);
        json.put("status", statusCode.value());
        json.put("statusText", statusCode.getReasonPhrase());
        return json;
    }

    protected ResponseEntity<Object> createJsonResponse(String message, Object data, Exception ex, HttpHeaders headers, HttpStatus statusCode, WebRequest request) {
        Map<String, Object> json = createJsonResponse(message, data, statusCode);
        return handleExceptionInternal(ex, json, headers, statusCode, request);
    }

    protected ResponseEntity<Object> handleDuplicateRelation(DuplicatedRelationException ex, WebRequest request) {
        return createJsonResponse(ex.getMessage(), null, ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    protected ResponseEntity<Object> handleInvalidEntity(InvalidEntityException ex, WebRequest request) {
        return createJsonResponse(ex.getMessage(), null, ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        return createJsonResponse(ex.getMessage(), null, ex, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    protected ResponseEntity<Object> handleRelatedEntityNotFound(RelatedEntityNotFoundException ex, WebRequest request) {
        return createJsonResponse(ex.getMessage(), null, ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    protected ResponseEntity<Object> handleIInvalidAction(InvalidActionException ex, WebRequest request) {
        return createJsonResponse(ex.getMessage(), null, ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        return createJsonResponse(
                "Fields are not valid",
                ex.getConstraintViolations()
                        .stream()
                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                        .toList(),
                ex,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request);
    }

    protected ResponseEntity<Object> handleGeneric(ResponseStatusException ex, WebRequest request) {
        return createJsonResponse(
                ex.getReason(),
            null,
                ex,
                new HttpHeaders(),
                HttpStatus.valueOf(ex.getStatusCode().value()), request);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {

        if(ex instanceof DuplicatedRelationException) {
            return handleDuplicateRelation((DuplicatedRelationException) ex, request);
        }

        if(ex instanceof InvalidEntityException) {
            return handleInvalidEntity((InvalidEntityException) ex, request);
        }

        if(ex instanceof EntityNotFoundException) {
            return handleEntityNotFound((EntityNotFoundException) ex, request);
        }

        if(ex instanceof RelatedEntityNotFoundException) {
            return handleRelatedEntityNotFound((RelatedEntityNotFoundException) ex, request);
        }

        if(ex instanceof InvalidActionException) {
            return handleIInvalidAction((InvalidActionException) ex, request);
        }

        if(ex instanceof ConstraintViolationException notValidException) {
            return handleConstraintViolation(notValidException, request);
        }

        if(ex instanceof ResponseStatusException responseStatusException) {
            return handleGeneric(responseStatusException, request);
        }

        return createJsonResponse("An error occurred", null, ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
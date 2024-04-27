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
// - DuplicatedRelationException (Custom)
// - InvalidEntityException (Custom)
// - EntityNotFoundException (Custom)
// - RelatedEntityNotFoundException (Custom)
// - ConstraintViolationException (Validation Errors)
// - ResponseStatusException (Generic)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> createJsonResponse(String message, Object data, Exception ex, HttpHeaders headers, HttpStatus statusCode, WebRequest request) {
        Map<String, Object> json = new HashMap<>();
        json.put("message", message);
        json.put("data", data);
        json.put("status", statusCode.value());
        json.put("statusText", statusCode.getReasonPhrase());
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
        return createJsonResponse(ex.getMessage(), null, ex, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
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

        if(ex instanceof ConstraintViolationException notValidException) {
            return handleConstraintViolation(notValidException, request);
        }

        if(ex instanceof ResponseStatusException responseStatusException) {
            return handleGeneric(responseStatusException, request);
        }

        return createJsonResponse("An error occurred", null, ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
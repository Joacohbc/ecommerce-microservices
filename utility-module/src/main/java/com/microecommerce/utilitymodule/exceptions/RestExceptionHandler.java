package com.microecommerce.utilitymodule.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> createResponse(String message, Object data, Exception ex, HttpHeaders headers, HttpStatus statusCode, WebRequest request) {
        Map<String, Object> json = new HashMap<>();
        json.put("message", message);
        json.put("data", data);
        json.put("status", statusCode.value());
        json.put("statusText", statusCode.getReasonPhrase());
        return handleExceptionInternal(ex, json, headers, statusCode, request);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {

        if(ex instanceof DuplicatedRelationException
            || ex instanceof InvalidEntityException) {
            return createResponse(ex.getMessage(), null, ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
        }

        if(ex instanceof EntityNotFoundException
                || ex instanceof RelatedEntityNotFoundException) {
            return createResponse(ex.getMessage(), null, ex, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
        }

        if(ex instanceof ConstraintViolationException notValidException) {
            return createResponse(
                    "Fields are not valid",
                    notValidException.getConstraintViolations()
                            .stream()
                            .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                            .toList(),
                    ex,
                    new HttpHeaders(),
                    HttpStatus.BAD_REQUEST,
                    request);
        }

        if(ex instanceof ResponseStatusException responseStatusException) {
            return createResponse(
                    responseStatusException.getReason(),
                null,
                    ex,
                    new HttpHeaders(),
                    HttpStatus.valueOf(responseStatusException.getStatusCode().value()), request);
        }

        return createResponse("An error occurred", null, ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
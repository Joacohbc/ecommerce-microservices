package com.microecommerce.productsservice.utils;

import java.util.HashMap;

public class JSONUtils {
    public static HashMap<String, Object> createResponse(String message, Object data) {
        var response = new HashMap<String, Object>();
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    public static HashMap<String, Object> createResponse(String message) {
        return createResponse(message, null);
    }

    public static HashMap<String, Object> createError(String error, Object data) {
        var response = new HashMap<String, Object>();
        response.put("error", error);
        response.put("data", data);
        return response;
    }

    public static HashMap<String, Object> createError(String error) {
        return createError(error, null);
    }
}

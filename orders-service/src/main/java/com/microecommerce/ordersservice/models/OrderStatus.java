package com.microecommerce.ordersservice.models;

public enum OrderStatus {
    CREATED("Created"),
    PROCESSING("Processing"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public static OrderStatus fromString(String status) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.name().equalsIgnoreCase(status)) {
                return orderStatus;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return status;
    }
}

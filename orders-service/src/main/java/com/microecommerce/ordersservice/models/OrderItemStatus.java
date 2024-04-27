package com.microecommerce.ordersservice.models;

public enum OrderItemStatus {
    CREATED("Created"),
    PROCESSING("Processing"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String status;

    OrderItemStatus(String status) {
        this.status = status;
    }

    public static OrderItemStatus fromString(String status) {
        for (OrderItemStatus orderStatus : OrderItemStatus.values()) {
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

package com.microecommerce.ordersservice.models;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED("Created", "Order submitted by customer, awaiting further processing"),
//    PAYMENT_PENDING("Payment Pending", "Order placed, awaiting authorization for the payment"),
//    PAYMENT_CONFIRMED("Payment Confirmed", "Payment has been authorized successfully"),
    IN_PROGRESS("In Progress", "Order processing initiated (picking, packing)"),
//    READY_FOR_SHIPMENT("Ready for Shipment", "Order prepared and awaiting carrier pickup"),
//    SHIPPED("Shipped", "Order shipped with carrier"),
//    OUT_FOR_DELIVERY("Out for Delivery", "Order in transit with the delivery carrier"),
//    DELIVERED("Delivered", "Order delivered successfully to customer"),
    CANCELLED("Cancelled", "Order cancelled by customer or merchant"),
    RETURN_REQUESTED("Return Requested", "Customer initiated a return request for the order"),
    RETURN_RECEIVED("Return Received", "Returned items have been received by the merchant"),
    REFUND_PENDING("Refund Pending", "Refund process initiated for a return"),
    REFUND_COMPLETED("Refund Completed", "Refund issued to the customer for a return"),
    INVOICE_ISSUED("Invoice Issued", "Invoice generated for the order");

    private final String name;
    private final String description;

    OrderStatus(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

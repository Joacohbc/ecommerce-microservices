package com.microecommerce.ordersservice.services.interfaces;

import com.microecommerce.ordersservice.models.Order;
import com.microecommerce.ordersservice.models.OrderProgressRequest;
import com.microecommerce.ordersservice.models.OrderStatus;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;

import java.util.List;

public interface IOrderService {
    List<Order> createBatch(List<Order> orders, Long customerId) throws InvalidEntityException;

    Order askForReturn(String orderId) throws InvalidEntityException;
    Order askForRefund(String orderId) throws InvalidEntityException;
    Order cancelOrder(String orderId) throws InvalidEntityException;
    Order finishOrder(String orderId, OrderStatus status) throws InvalidEntityException;
    List<Order> startProcessingOrder(List<OrderProgressRequest> requests) throws InvalidEntityException;

    Order getById(String orderId) throws InvalidEntityException;
    List<Order> getAllCustomerOrders(Long customerId);
    List<Order> getAllOrders();
}

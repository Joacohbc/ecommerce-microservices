package com.microecommerce.ordersservice.services.interfaces;

import com.microecommerce.ordersservice.models.Order;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;

import java.util.List;

public interface IOrderService {
    Order createOrder(Order order) throws InvalidEntityException;
    List<Order> createBatch(List<Order> orders);
    List<Order> getAllCustomerOrders(Long customerId);
    List<Order> getAllOrders();
}

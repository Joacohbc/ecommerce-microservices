package com.microecommerce.ordersservice.controllers;

import com.microecommerce.ordersservice.models.Order;
import com.microecommerce.ordersservice.models.OrderHistory;
import com.microecommerce.ordersservice.models.OrderProgressRequest;
import com.microecommerce.ordersservice.models.OrderStatus;
import com.microecommerce.ordersservice.services.interfaces.IOrderService;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final IOrderService orderService;

    @Autowired
    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) throws InvalidEntityException {
        // TODO: Implement customer id extraction from security context
        return orderService.createBatch(List.of(order), 0L).get(0);
    }

    @PostMapping("/batch")
    public List<Order> createBatch(@RequestBody List<Order> orders) throws InvalidEntityException {
        // TODO: Implement customer id extraction from security context
        return orderService.createBatch(orders, 0L);
    }

    // TODO: ONLY FOR TESTING PURPOSES
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/{orderId}/return")
    public Order askForReturn(@PathVariable String orderId) throws InvalidEntityException {
        return orderService.askForReturn(orderId);
    }

    @PutMapping("/{orderId}/refund")
    public Order askForRefund(@PathVariable String orderId) throws InvalidEntityException {
        return orderService.askForRefund(orderId);
    }

    @PutMapping("/{orderId}/cancel")
    public Order askForCancellation(@PathVariable String orderId) throws InvalidEntityException {
        return orderService.cancelOrder(orderId);
    }

    @PutMapping("/{orderId}/finish")
    public Order finishOrder(@PathVariable String orderId, @RequestParam OrderStatus status) throws InvalidEntityException {
        return orderService.finishOrder(orderId, status);
    }

    @PutMapping("/in-progress")
    public List<Order> putInProgress(@RequestBody List<OrderProgressRequest> requests) throws InvalidEntityException {
        return orderService.startProcessingOrder(requests);
    }

    @GetMapping("/{orderId}/history")
    public List<OrderHistory> getOrderHistory(@PathVariable String orderId) {
        return orderService.getOrderHistory(orderId);
    }

    @GetMapping("/{orderId}/checkStatus")
    public Order getOrderById(@PathVariable String orderId) throws InvalidEntityException {
        return orderService.getById(orderId);
    }

    @GetMapping("/customer/{customerId}")
    public List<Order> getAllCustomerOrders(@PathVariable Long customerId) {
        return orderService.getAllCustomerOrders(customerId);
    }
}

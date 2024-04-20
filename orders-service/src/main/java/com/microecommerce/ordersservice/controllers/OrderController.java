package com.microecommerce.ordersservice.controllers;

import com.microecommerce.ordersservice.models.Order;
import com.microecommerce.ordersservice.services.interfaces.IOrderService;
import com.microecommerce.productsservice.exceptions.InvalidEntityException;
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
        return orderService.createOrder(order);
    }

    @PostMapping("/batch")
    public List<Order> createBatch(@RequestBody List<Order> orders) {
        return orderService.createBatch(orders);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/customer/{customerId}")
    public List<Order> getAllCustomerOrders(@PathVariable Long customerId) {
        return orderService.getAllCustomerOrders(customerId);
    }
}

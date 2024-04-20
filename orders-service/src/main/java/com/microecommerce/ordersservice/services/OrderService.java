package com.microecommerce.ordersservice.services;

import com.microecommerce.ordersservice.repositories.OrderRepository;
import com.microecommerce.ordersservice.services.interfaces.IOrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


}

package com.microecommerce.ordersservice.services;

import com.microecommerce.ordersservice.models.Item;
import com.microecommerce.ordersservice.models.Order;
import com.microecommerce.ordersservice.repositories.OrderRepository;
import com.microecommerce.ordersservice.services.interfaces.IOrderService;
import com.microecommerce.productsservice.exceptions.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductFeignService productService;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductFeignService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    @Override
    public Order createOrder(Order order) throws InvalidEntityException {
        boolean productsExistence = productService.checkProductsExistence(order
                .getItems()
                .stream()
                .map(Item::getProductId)
                .toList());

        if (!productsExistence) throw new InvalidEntityException("Some products do not exist");

        return orderRepository.insert(order);
    }

    @Override
    public List<Order> createBatch(List<Order> orders) {
        return orderRepository.insert(orders);
    }

    @Override
    public List<Order> getAllCustomerOrders(Long customerId) {
        return orderRepository.findAllByCustomerId(customerId);
    }
}

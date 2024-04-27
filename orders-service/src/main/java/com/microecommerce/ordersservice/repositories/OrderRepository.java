package com.microecommerce.ordersservice.repositories;

import com.microecommerce.ordersservice.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findAllByCustomerId(Long customerId);
    List<Order> findAllByCreatedAtBefore(LocalDateTime date);
}

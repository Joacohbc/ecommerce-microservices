package com.microecommerce.ordersservice.repositories;

import com.microecommerce.ordersservice.models.OrderHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends MongoRepository<OrderHistory, String> {
}

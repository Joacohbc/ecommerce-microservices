package com.microecommerce.ordersservice.repositories;

import com.microecommerce.ordersservice.models.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, Long> {}

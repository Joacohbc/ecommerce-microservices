package com.microecommerce.ordersservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

// Exclude DataSourceAutoConfiguration to avoid DataSource bean creation
// because we are using FeignClient to communicate with other services (and no using JPA EntityManager, only MongoDB)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableFeignClients
public class OrdersServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrdersServiceApplication.class, args);
    }
}
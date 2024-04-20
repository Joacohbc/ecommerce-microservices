package com.microecommerce.ordersservice.models;

import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document("orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order implements Serializable {

    @Id
    private Long id;
    private List<Item> items;
    private Double total;

    @CreationTimestamp
    private String createdAt;

    @UpdateTimestamp
    private String updatedAt;
}

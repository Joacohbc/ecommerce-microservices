package com.microecommerce.ordersservice.models;

import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Document("orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order implements Serializable {

    @Id
    private String id;

    private Long customerId;

    private List<OrderItem> items;

    private OrderItemStatus status;

    private Double total;

    private LocalDateTime finishedAt;

    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return new ObjectId(id).
                getDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}

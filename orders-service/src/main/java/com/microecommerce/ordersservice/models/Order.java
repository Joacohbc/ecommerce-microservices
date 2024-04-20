package com.microecommerce.ordersservice.models;

import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import lombok.*;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Document("orders")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order implements Serializable {

    @Id
    private String id;

    @Setter
    private Long customerId;

    @Setter
    private List<Item> items;

    @Setter
    private OrderStatus status;

    @Setter
    private Double total;

    @Setter
    private LocalDateTime finishedAt;

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

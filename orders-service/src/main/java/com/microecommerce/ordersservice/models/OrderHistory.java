package com.microecommerce.ordersservice.models;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Document("order_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderHistory {

    @Id
    private String id;

    @NotEmpty
    private String orderId;

    @NotNull
    private OrderStatus status;

    private String description;

    public LocalDateTime getCreatedAt() {
        return new ObjectId(id).
                getDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}

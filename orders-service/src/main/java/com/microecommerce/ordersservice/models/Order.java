package com.microecommerce.ordersservice.models;

import com.microecommerce.utilitymodule.models.TimeStamped;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private Long customerId;

    private Long discountId;

    private Long paymentId;

    private Long shippingId;

    @NotEmpty
    private List<OrderItem> items;

    @NotNull
    private OrderStatus status;

    private Double total;

    private LocalDateTime processedAt;
    private LocalDateTime finishedAt;

    private TimeStamped timeStamp;

    public LocalDateTime getCreatedAt() {
        return new ObjectId(id).
                getDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}

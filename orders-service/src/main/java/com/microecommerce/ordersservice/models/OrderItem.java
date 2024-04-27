package com.microecommerce.ordersservice.models;

import com.microecommerce.dtoslibrary.products_service.ProductDTO;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem implements Serializable {

    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;

    private Double price;

    @Transient
    private ProductDTO product;
}

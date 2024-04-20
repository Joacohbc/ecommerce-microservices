package com.microecommerce.ordersservice.models;

import com.microecommerce.productsservice.models.Product;
import jakarta.persistence.Transient;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item implements Serializable {

    private Long productId;
    private Integer quantity;

    @Transient
    private Product product;
}

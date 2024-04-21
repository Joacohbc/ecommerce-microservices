package com.microecommerce.ordersservice.models;

import com.microecommerce.dtoslibrary.products_service.ProductDTO;
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
    private Double price;

    @Transient
    private ProductDTO product;
}

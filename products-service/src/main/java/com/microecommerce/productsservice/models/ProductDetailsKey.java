package com.microecommerce.productsservice.models;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Data;

@Embeddable
@Data
// Is a composite key for the product details table
public class ProductDetailsKey {
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "detail_id")
    private Long detailId;
}

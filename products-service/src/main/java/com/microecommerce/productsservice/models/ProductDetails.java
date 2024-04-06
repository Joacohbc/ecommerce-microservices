package com.microecommerce.productsservice.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
// The ProductDetails entity have a composite key to join the Product with the Detail information
// and represents details from a products in the system. Have the objective to store the value of
// the Details of a specific product, and optionally additional information.
public class ProductDetails {

    public enum Type {
        STRING,
        NUMBER,
        BOOLEAN,
        DATE
    }

    @EmbeddedId
    private ProductDetailsKey id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @MapsId("productId")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "detail_id")
    @MapsId("detailId")
    private Detail detail;

    @Column(nullable = false)
    private String value;

    @Column(length = 500)
    private String additionalInfo;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Type type;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Fast access to detail name
    public String getDetailName() {
        return detail.getName();
    }

    // Fast access to detail description
    public String getDetailDescription() {
        return detail.getDescription();
    }
 }

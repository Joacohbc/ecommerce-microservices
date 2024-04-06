package com.microecommerce.productsservice.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
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

    public String getDetailName() {
        return detail.getName();
    }

    public String getDetailDescription() {
        return detail.getDescription();
    }
 }

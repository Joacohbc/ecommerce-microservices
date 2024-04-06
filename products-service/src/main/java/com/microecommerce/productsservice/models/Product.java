package com.microecommerce.productsservice.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
// Represents a product in the system and contains all the information about it.
public class Product implements Serializable {

    public Product() {
        this.brands = new LinkedList<>();
        this.tags = new LinkedList<>();
        this.categories = new LinkedList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false, length = 500)
    private String name;

    @Column(nullable = false, length = 50000)
    private String description;

    @Column(nullable = false)
    private Double originalPrice;

    @Column(nullable = false)
    private Boolean isDeleted;

    @PrePersist
    void preInsert() {
        if (this.isDeleted == null) this.isDeleted = false;
    }

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(updatable = false)
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Category> categories;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Brand> brands;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Tag> tags;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, mappedBy = "product")
    private List<ProductDetails> productDetails;
}

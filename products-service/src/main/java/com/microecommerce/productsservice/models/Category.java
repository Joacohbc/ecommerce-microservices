package com.microecommerce.productsservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
// Represents a category for a product, which can be used to have general information to group products.
// Like a Tag, but with a more general purpose.
// Example: "Electronics", "Clothing", "Books", etc.
public class Category implements Serializable, IGetId {

    public Category() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String name;

    @Column(nullable = false, length = 1000)
    @NotBlank
    @Size(max = 1000)
    private String description;
}

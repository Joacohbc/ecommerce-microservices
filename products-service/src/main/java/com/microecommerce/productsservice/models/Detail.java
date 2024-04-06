package com.microecommerce.productsservice.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500, unique = true)
    private String name;

    @Column(nullable = false, length = 5000)
    private String description;
}

package com.microecommerce.productsservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.io.Serializable;

@Entity
@Data
// Represents a brand. A brand is a company that produces products.
public class Brand implements Serializable, IGetId {

    public Brand() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Size(max = 255)
    @Convert(converter = ToTitleCase.class)
    // TODO: Check if this should be Unique or Not (because two companies can have the same name)
    private String name;
}

package com.microecommerce.productsservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
// Represents a tag for a product, which can be used to have more-specific information to group products.
// Like a Category, but with a more specific purpose.
// Example: "Smartphone", "Laptop", "Fiction", etc.
public class Tag implements Serializable, IGetId {

    public Tag() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Size(max = 255)
    @Convert(converter = ToTitleCase.class)
    // TODO: Check if this should be Unique or Not (because two different tags can have the same name but different descriptions)
    private String name;

    @Column(nullable = false, length = 500)
    @NotBlank
    @Size(max = 500)
    private String description;

//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
//    private Tag parentTag;

//    @OneToMany(mappedBy = "parentTag", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    private List<Tag> childTags;
}

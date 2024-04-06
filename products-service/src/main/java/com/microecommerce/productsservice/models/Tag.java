package com.microecommerce.productsservice.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Tag implements Serializable {

    public Tag() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Tag parentTag;

    @OneToMany(mappedBy = "parentTag", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Tag> childTags;
}

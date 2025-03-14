package com.microecommerce.productsservice.models;

import com.microecommerce.utilitymodule.interfaces.IGetId;
import com.microecommerce.utilitymodule.models.TimeStamped;
import com.microecommerce.utilitymodule.models.ToTitleCase;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
// Represents a product in the system and contains all the information about it.
public class Product implements Serializable, IGetId {

    public Product() {
        this.brands = new LinkedList<>();
        this.tags = new LinkedList<>();
        this.categories = new LinkedList<>();
    }

    @Getter
    public enum ProductFields {
        SKU("sku"),
        NAME("name"),
        ORIGINAL_PRICE("originalPrice"),
        IS_DELETED("isDeleted"),
        CREATED_AT("createdAt"),
        UPDATED_AT("updatedAt");

        private final String name;

        public static ProductFields fromName(String name) {
            for (ProductFields field : ProductFields.values()) {
                if (field.getName().equalsIgnoreCase(name)) {
                    return field;
                }
            }
            return null;
        }

        ProductFields(String name) {
            this.name = name;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Size(min = 3, max = 255)
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "must be alphanumeric")
    private String sku;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 3, max = 255)
    @Convert(converter = ToTitleCase.class)
    private String name;

    @Column(nullable = false, length = 50000)
    @NotBlank
    @Size(min = 3, max = 50000)
    private String description;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Double originalPrice;

    @Column(nullable = false)
    private Boolean isDeleted;

    @PrePersist
    void preInsert() {
        if (this.isDeleted == null) this.isDeleted = false;
    }

    private LocalDateTime deletedAt;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Category> categories;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Brand> brands;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Tag> tags;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, mappedBy = "product")
    private List<ProductDetails> productDetails;

    @Embedded
    private TimeStamped timeStamp;
}

package com.microecommerce.productsservice.models;

import com.microecommerce.utilitymodule.interfaces.IGetId;
import com.microecommerce.utilitymodule.models.TimeStamped;
import com.microecommerce.utilitymodule.models.ToTitleCase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
// Represents details of different products, which can be used to provide more information about the product.
// In practice, it represents a "detail type" because the value of the detail information itself is stored in the
// ProductDetails entity separate from the Detail. In this way, the Detail entity is used to store the name and
// description of the detail, and ProductDetail stores the specific value for each product.
public class Detail implements Serializable, IGetId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Size(max = 255)
    @Convert(converter = ToTitleCase.class)
    private String name;

    @Column(nullable = false, length = 5000)
    @NotBlank
    @Size(max = 5000)
    private String description;

    @Embedded
    private TimeStamped timeStamp;
}

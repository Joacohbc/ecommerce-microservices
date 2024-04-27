package com.microecommerce.dtoslibrary.products_service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private Double originalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Boolean isDeleted;
    private List<CategoryDTO> categories;
    private List<BrandDTO> brands;
    private List<TagDTO> tags;
    private List<ProductDetailsDTO> productDetails;
}

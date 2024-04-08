package com.microecommerce.productsservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private List<CategoryDTO> categories;
    private List<BrandDTO> brands;
    private List<TagDTO> tags;
    private List<ProductDetailsDTO> productDetails;
}

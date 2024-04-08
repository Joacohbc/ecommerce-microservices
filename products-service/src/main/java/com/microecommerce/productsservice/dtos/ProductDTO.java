package com.microecommerce.productsservice.dtos;

import com.microecommerce.productsservice.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

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

    public static ProductDTO fromEntity(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .originalPrice(product.getOriginalPrice())
                .categories(CategoryDTO.fromEntities(product.getCategories()))
                .brands(BrandDTO.fromEntities(product.getBrands()))
                .tags(TagDTO.fromEntities(product.getTags()))
                .productDetails(ProductDetailsDTO.fromEntities(product.getProductDetails()))
                .build();
    }

    public static Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setSku(productDTO.getSku());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setOriginalPrice(productDTO.getOriginalPrice());
        product.setCategories(CategoryDTO.toEntities(productDTO.getCategories()));
        product.setBrands(BrandDTO.toEntities(productDTO.getBrands()));
        product.setTags(TagDTO.toEntities(productDTO.getTags()));
        product.setProductDetails(ProductDetailsDTO.toEntities(productDTO.getProductDetails(), productDTO.getId()));
        return product;
    }

    public static List<ProductDTO> fromEntities(List<Product> products) {
        return products.stream().map(ProductDTO::fromEntity).collect(Collectors.toList());
    }

    public static List<Product> toEntities(List<ProductDTO> productDTOs) {
        return productDTOs.stream().map(ProductDTO::toEntity).collect(Collectors.toList());
    }
}

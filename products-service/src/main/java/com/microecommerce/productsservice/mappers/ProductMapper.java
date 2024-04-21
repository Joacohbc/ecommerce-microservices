package com.microecommerce.productsservice.mappers;

import com.microecommerce.dtoslibrary.products_service.ProductDTO;
import com.microecommerce.dtoslibrary.products_service.TagDTO;
import com.microecommerce.productsservice.models.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {
    public static ProductDTO fromEntity(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .originalPrice(product.getOriginalPrice())
                .categories(CategoryMapper.fromEntities(product.getCategories()))
                .brands(BrandMapper.fromEntities(product.getBrands()))
                .tags(TagMapper.fromEntities(product.getTags()))
                .productDetails(ProductDetailsMapper.fromEntities(product.getProductDetails()))
                .createdAt(product.getCreatedAt()) // Only Getters (not mapped to entity)
                .updatedAt(product.getUpdatedAt()) // Only Getters (not mapped to entity)
                .deletedAt(product.getDeletedAt()) // Only Getters (not mapped to entity)
                .isDeleted(product.getIsDeleted()) // Only Getters (not mapped to entity)
                .build();
    }

    public static Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setSku(productDTO.getSku());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setOriginalPrice(productDTO.getOriginalPrice());
        product.setCategories(CategoryMapper.toEntities(productDTO.getCategories()));
        product.setBrands(BrandMapper.toEntities(productDTO.getBrands()));
        product.setTags(TagMapper.toEntities(productDTO.getTags()));
        product.setProductDetails(ProductDetailsMapper.toEntities(productDTO.getProductDetails(), productDTO.getId()));
        return product;
    }

    public static List<ProductDTO> fromEntities(List<Product> products) {
        return products.stream().map(ProductMapper::fromEntity).collect(Collectors.toList());
    }

    public static List<Product> toEntities(List<ProductDTO> productDTOs) {
        return productDTOs.stream().map(ProductMapper::toEntity).collect(Collectors.toList());
    }

}

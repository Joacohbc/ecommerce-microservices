package com.microecommerce.productsservice.dtos;

import com.microecommerce.productsservice.models.Detail;
import com.microecommerce.productsservice.models.Product;
import com.microecommerce.productsservice.models.ProductDetails;
import com.microecommerce.productsservice.models.ProductDetailsKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsDTO {
    private Long detailId;
    private String detailName;
    private Object value;
    private String additionalInfo;

    public static ProductDetailsDTO fromEntity(ProductDetails productDetails) {
        return ProductDetailsDTO.builder()
                .detailId(productDetails.getDetail().getId())
                .value(productDetails.getValue())
                .additionalInfo(productDetails.getAdditionalInfo())
                .detailName(productDetails.getDetail().getName()) // Only Getters
                .build();
    }

    public static ProductDetails toEntity(ProductDetailsDTO productDetailsDTO, Long productId) {
        ProductDetails productDetails = new ProductDetails();

        ProductDetailsKey productDetailsKey = new ProductDetailsKey();
        productDetailsKey.setProductId(productId);
        productDetailsKey.setDetailId(productDetailsDTO.getDetailId());

        Detail detail = new Detail();
        detail.setId(productDetailsDTO.getDetailId());
        productDetails.setDetail(detail);

        Product product = new Product();
        product.setId(productId);
        productDetails.setProduct(product);

        productDetails.setValue(productDetailsDTO.getValue());
        productDetails.setAdditionalInfo(productDetailsDTO.getAdditionalInfo());
        return productDetails;
    }

    public static List<ProductDetailsDTO> fromEntities(List<ProductDetails> productDetails) {
        return productDetails.stream().map(ProductDetailsDTO::fromEntity).collect(Collectors.toList());
    }

    public static List<ProductDetails> toEntities(List<ProductDetailsDTO> productDetailsDTOs, Long productId) {
        return productDetailsDTOs.stream().map(productDetailsDTO -> toEntity(productDetailsDTO, productId)).collect(Collectors.toList());
    }
}

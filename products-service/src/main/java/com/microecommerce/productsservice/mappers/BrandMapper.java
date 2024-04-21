package com.microecommerce.productsservice.mappers;

import com.microecommerce.dtoslibrary.products_service.BrandDTO;
import com.microecommerce.productsservice.models.Brand;

import java.util.List;
import java.util.stream.Collectors;

public class BrandMapper {
    public static BrandDTO fromEntity(Brand brand) {
        return BrandDTO.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }

    public static Brand toEntity(BrandDTO brandDTO) {
        Brand brand = new Brand();
        brand.setId(brandDTO.getId());
        brand.setName(brandDTO.getName());
        return brand;
    }

    public static List<BrandDTO> fromEntities(List<Brand> brands) {
        return brands.stream().map(BrandMapper::fromEntity).collect(Collectors.toList());
    }

    public static List<Brand> toEntities(List<BrandDTO> brandDTOs) {
        return brandDTOs.stream().map(BrandMapper::toEntity).collect(Collectors.toList());
    }
}

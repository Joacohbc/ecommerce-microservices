package com.microecommerce.productsservice.dtos;

import com.microecommerce.productsservice.models.Brand;
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
public class BrandDTO {
    private Long id;
    private String name;

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
        return brands.stream().map(BrandDTO::fromEntity).collect(Collectors.toList());
    }

    public static List<Brand> toEntities(List<BrandDTO> brandDTOs) {
        return brandDTOs.stream().map(BrandDTO::toEntity).collect(Collectors.toList());
    }
}

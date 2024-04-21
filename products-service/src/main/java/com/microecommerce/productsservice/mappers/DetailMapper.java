package com.microecommerce.productsservice.mappers;

import com.microecommerce.dtoslibrary.products_service.DetailDTO;
import com.microecommerce.productsservice.models.Detail;

import java.util.List;
import java.util.stream.Collectors;

public class DetailMapper {
    public static DetailDTO fromEntity(Detail detail) {
        return DetailDTO.builder()
                .id(detail.getId())
                .name(detail.getName())
                .description(detail.getDescription())
                .build();
    }

    public static Detail toEntity(DetailDTO detailDTO) {
        Detail detail = new Detail();
        detail.setId(detailDTO.getId());
        detail.setName(detailDTO.getName());
        detail.setDescription(detailDTO.getDescription());
        return detail;
    }

    public static List<DetailDTO> fromEntities(List<Detail> details) {
        return details.stream().map(DetailMapper::fromEntity).collect(Collectors.toList());
    }

    public static List<Detail> toEntities(List<DetailDTO> detailDTOs) {
        return detailDTOs.stream().map(DetailMapper::toEntity).collect(Collectors.toList());
    }
}

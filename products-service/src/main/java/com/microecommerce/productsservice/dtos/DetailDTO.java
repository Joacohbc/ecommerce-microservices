package com.microecommerce.productsservice.dtos;

import com.microecommerce.productsservice.models.Detail;
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
public class DetailDTO {
    private Long id;
    private String name;
    private String description;

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
        return details.stream().map(DetailDTO::fromEntity).collect(Collectors.toList());
    }

    public static List<Detail> toEntities(List<DetailDTO> detailDTOs) {
        return detailDTOs.stream().map(DetailDTO::toEntity).collect(Collectors.toList());
    }
}

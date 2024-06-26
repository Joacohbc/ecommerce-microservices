package com.microecommerce.dtoslibrary.products_service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailDTO {
    private Long id;
    private String name;
    private String description;
}

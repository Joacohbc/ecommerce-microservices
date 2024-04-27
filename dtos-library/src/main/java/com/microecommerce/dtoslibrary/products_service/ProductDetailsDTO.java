package com.microecommerce.dtoslibrary.products_service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsDTO {
    private Long detailId;
    private String detailName;
    private Object value;
    private String additionalInfo;
}

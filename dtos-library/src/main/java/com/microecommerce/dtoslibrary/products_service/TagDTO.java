package com.microecommerce.dtoslibrary.products_service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {
    private Long id;
    private String name;
    private String description;
//    private TagDTO parentTag;
}

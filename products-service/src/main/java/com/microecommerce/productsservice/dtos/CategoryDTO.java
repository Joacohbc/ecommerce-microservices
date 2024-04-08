package com.microecommerce.productsservice.dtos;

import com.microecommerce.productsservice.models.Category;
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
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;

    public static CategoryDTO fromEntity(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public static Category toEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return category;
    }

    public static List<CategoryDTO> fromEntities(List<Category> categories) {
        return categories.stream().map(CategoryDTO::fromEntity).collect(Collectors.toList());
    }

    public static List<Category> toEntities(List<CategoryDTO> categoryDTOs) {
        return categoryDTOs.stream().map(CategoryDTO::toEntity).collect(Collectors.toList());
    }
}

package com.microecommerce.productsservice.mappers;

import com.microecommerce.dtoslibrary.products_service.CategoryDTO;
import com.microecommerce.productsservice.models.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

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
        return categories.stream().map(CategoryMapper::fromEntity).collect(Collectors.toList());
    }

    public static List<Category> toEntities(List<CategoryDTO> categoryDTOs) {
        return categoryDTOs.stream().map(CategoryMapper::toEntity).collect(Collectors.toList());
    }
}

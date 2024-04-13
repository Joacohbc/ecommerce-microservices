package com.microecommerce.productsservice.controllers;

import com.microecommerce.productsservice.dtos.CategoryDTO;
import com.microecommerce.productsservice.exceptions.DuplicatedRelationException;
import com.microecommerce.productsservice.exceptions.EntityNotFoundException;
import com.microecommerce.productsservice.exceptions.InvalidEntityException;
import com.microecommerce.productsservice.exceptions.RelatedEntityNotFoundException;
import com.microecommerce.productsservice.services.interfaces.ICategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return CategoryDTO.fromEntities(categoryService.getAll());
    }

    @GetMapping("/{id}")
    public CategoryDTO getCategoryById(@PathVariable Long id) throws EntityNotFoundException {
        return CategoryDTO.fromEntity(categoryService.getById(id));
    }

    @PostMapping
    public CategoryDTO addCategory(@RequestBody CategoryDTO category) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        return addCategories(Collections.singletonList(category)).get(0);
    }

    @PostMapping("/batch")
    public List<CategoryDTO> addCategories(@RequestBody List<CategoryDTO> categories) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        return CategoryDTO.fromEntities(categoryService.createBatch(CategoryDTO.toEntities(categories)));
    }

    @PutMapping("/{id}")
    public CategoryDTO updateCategory(@PathVariable Long id, @RequestBody CategoryDTO category) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException, InvalidEntityException {
        category.setId(id);
        return updateCategories(Collections.singletonList(category)).get(0);
    }

    @PutMapping("/batch")
    public List<CategoryDTO> updateCategories(@RequestBody List<CategoryDTO> categories) throws EntityNotFoundException, DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException{
        return CategoryDTO.fromEntities(categoryService.updateBatch(CategoryDTO.toEntities(categories)));
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) throws EntityNotFoundException {
        categoryService.deleteById(id);
    }
}

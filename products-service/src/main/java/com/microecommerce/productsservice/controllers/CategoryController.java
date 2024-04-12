package com.microecommerce.productsservice.controllers;

import com.microecommerce.productsservice.exceptions.NoRelatedEntityException;
import com.microecommerce.productsservice.models.Category;
import com.microecommerce.productsservice.services.CategoryService;
import com.microecommerce.productsservice.services.interfaces.ICategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAll();
    }

//    @GetMapping("/{id}")
//    public Category getCategoryById(@PathVariable Long id) {
//        return categoryService.getById(id);
//    }

    @PostMapping
    // TODO: Internally manage Exception
    public Category addCategory(@RequestBody Category category) throws Exception {
        return categoryService.create(category);
    }

    @PostMapping("/batch")
    // TODO: Internally manage Exception
    public List<Category> addCategories(@RequestBody List<Category> categories) throws Exception {
        return categoryService.createBatch(categories);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        return categoryService.update(category);
    }

    @PutMapping("/batch")
    public List<Category> updateCategories(@RequestBody List<Category> categories) {
        return categoryService.updateBatch(categories);
    }

//    @DeleteMapping("/{id}")
//    public void deleteCategory(@PathVariable Long id) {
//        categoryService.deleteById(id);
//    }
}

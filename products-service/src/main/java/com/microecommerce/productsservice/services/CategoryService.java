package com.microecommerce.productsservice.services;

import com.microecommerce.productsservice.exceptions.EntityNotFoundException;
import com.microecommerce.productsservice.models.Category;
import com.microecommerce.productsservice.repositories.CategoryRepository;
import com.microecommerce.productsservice.services.interfaces.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(Long id) throws EntityNotFoundException {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    @Override
    public List<Category> getByIds(List<Long> ids) {
        return categoryRepository.findAllById(ids);
    }

    @Override
    public Category create(Category entity) {
        return categoryRepository.save(entity);
    }

    @Override
    public List<Category> createBatch(List<Category> entities) {
        return categoryRepository.saveAll(entities);
    }

    @Override
    public Category update(Category entity) {
        return categoryRepository.save(entity);
    }

    @Override
    public List<Category> updateBatch(List<Category> entities) {
        return categoryRepository.saveAll(entities);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}

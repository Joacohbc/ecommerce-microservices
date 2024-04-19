package com.microecommerce.productsservice.services;

import com.microecommerce.productsservice.exceptions.EntityNotFoundException;
import com.microecommerce.productsservice.exceptions.InvalidEntityException;
import com.microecommerce.productsservice.models.Category;
import com.microecommerce.productsservice.models.IGetId;
import com.microecommerce.productsservice.repositories.CategoryRepository;
import com.microecommerce.productsservice.services.interfaces.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
    public Category create(@Valid Category entity) throws InvalidEntityException {
        return createBatch(Collections.singletonList(entity)).get(0);
    }

    @Override
    public List<Category> createBatch(@Valid List<Category> entities) throws InvalidEntityException {
        if(entities.isEmpty()) return Collections.emptyList();
        entities.forEach(category -> category.setId(null));
        validateDuplicatedNames(entities);
        return categoryRepository.saveAll(entities);
    }

    @Override
    public Category update(@Valid Category entity) throws InvalidEntityException {
        return updateBatch(Collections.singletonList(entity)).get(0);
    }

    @Override
    public List<Category> updateBatch(@Valid List<Category> entities) throws InvalidEntityException {
        if(entities.isEmpty()) return Collections.emptyList();
        if(!IGetId.allHaveId(entities)) throw new InvalidEntityException("All categories must have an ID to be updated");

        validateDuplicatedNames(entities);
        return categoryRepository.saveAll(entities);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    private void validateDuplicatedNames(List<Category> categories) throws InvalidEntityException {
        var names = categories.stream().map(Category::getName).collect(Collectors.toList());
        var repeatedName = categoryRepository.existsByNameIn(names);
        if(repeatedName) throw new InvalidEntityException("Some categories have repeated name at database");

        // Check if there are repeated Name in the request (only if there are more than one product)
        if(!categories.isEmpty() && (categories.size() != new HashSet<>(names).size()))
            throw new InvalidEntityException("Some categories have repeated name in the request");
    }
}

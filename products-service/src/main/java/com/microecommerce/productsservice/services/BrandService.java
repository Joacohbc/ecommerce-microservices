package com.microecommerce.productsservice.services;

import com.microecommerce.productsservice.exceptions.EntityNotFoundException;
import com.microecommerce.productsservice.models.Brand;
import com.microecommerce.productsservice.repositories.BrandRepository;
import com.microecommerce.productsservice.repositories.ProductRepository;
import com.microecommerce.productsservice.services.interfaces.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService implements IBrandService {
    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> getAll() {
        return brandRepository.findAll();
    }

    @Override
    public Brand getById(Long id) throws EntityNotFoundException {
        return brandRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Brand not found"));
    }

    @Override
    public List<Brand> getByIds(List<Long> ids) {
        return brandRepository.findAllByIdIn(ids);
    }

    @Override
    public Brand create(Brand entity) {
        return brandRepository.save(entity);
    }

    @Override
    public List<Brand> createBatch(List<Brand> entities) {
        return brandRepository.saveAll(entities);
    }

    @Override
    public Brand update(Brand entity) {
        return brandRepository.save(entity);
    }

    @Override
    public List<Brand> updateBatch(List<Brand> entities) {
        return brandRepository.saveAll(entities);
    }

    @Override
    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }
}

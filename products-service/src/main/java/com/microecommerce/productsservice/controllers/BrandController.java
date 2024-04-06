package com.microecommerce.productsservice.controllers;


import com.microecommerce.productsservice.exceptions.NoRelatedEntityException;
import com.microecommerce.productsservice.models.Brand;
import com.microecommerce.productsservice.services.BrandService;
import com.microecommerce.productsservice.services.interfaces.IBrandService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {
    private final IBrandService brandService;

    public BrandController(IBrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public List<Brand> getAllBrands() {
        return brandService.getAll();
    }

    @GetMapping("/{id}")
    public Brand getBrandById(@PathVariable Long id) {
        return brandService.getById(id);
    }

    @PostMapping
    public Brand addBrand(@RequestBody Brand brand) throws NoRelatedEntityException {
        return brandService.create(brand);
    }

    @PostMapping("/batch")
    public List<Brand> addBrands(@RequestBody List<Brand> brands) throws NoRelatedEntityException {
        return brandService.createBatch(brands);
    }

    @PutMapping("/{id}")
    public Brand updateBrand(@PathVariable Long id, @RequestBody Brand brand) {
        brand.setId(id);
        return brandService.update(brand);
    }

    @PutMapping("/batch")
    public List<Brand> updateBrands(@RequestBody List<Brand> brands) {
        return brandService.updateBatch(brands);
    }

    @DeleteMapping("/{id}")
    public void deleteBrand(@PathVariable Long id) {
        brandService.deleteById(id);
    }
}

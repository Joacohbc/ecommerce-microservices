package com.microecommerce.productsservice.controllers;


import com.microecommerce.productsservice.dtos.BrandDTO;
import com.microecommerce.productsservice.exceptions.DuplicatedRelationException;
import com.microecommerce.productsservice.exceptions.EntityNotFoundException;
import com.microecommerce.productsservice.exceptions.InvalidEntityException;
import com.microecommerce.productsservice.exceptions.RelatedEntityNotFoundException;
import com.microecommerce.productsservice.services.interfaces.IBrandService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {
    private final IBrandService brandService;

    public BrandController(IBrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public List<BrandDTO> getAllBrands() {
        return BrandDTO.fromEntities(brandService.getAll());
    }

    @GetMapping("/{id}")
    public BrandDTO getBrandById(@PathVariable Long id) throws EntityNotFoundException {
        return BrandDTO.fromEntity(brandService.getById(id));
    }

    @PostMapping
    public BrandDTO addBrand(@RequestBody BrandDTO brand) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        return addBrands(Collections.singletonList(brand)).get(0);
    }

    @PostMapping("/batch")
    public List<BrandDTO> addBrands(@RequestBody List<BrandDTO> brands) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        return BrandDTO.fromEntities(brandService.createBatch(BrandDTO.toEntities(brands)));
    }

    @PutMapping("/{id}")
    public BrandDTO updateBrand(@PathVariable Long id, @RequestBody BrandDTO brand) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException, InvalidEntityException {
        brand.setId(id);
        return updateBrands(Collections.singletonList(brand)).get(0);
    }

    @PutMapping("/batch")
    public List<BrandDTO> updateBrands(@RequestBody List<BrandDTO> brands) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException, InvalidEntityException {
        return BrandDTO.fromEntities(brandService.updateBatch(BrandDTO.toEntities(brands)));
    }

    @DeleteMapping("/{id}")
    public void deleteBrand(@PathVariable Long id) throws EntityNotFoundException {
        brandService.deleteById(id);
    }
}

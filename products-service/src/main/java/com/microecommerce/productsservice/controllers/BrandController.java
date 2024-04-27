package com.microecommerce.productsservice.controllers;


import com.microecommerce.dtoslibrary.products_service.BrandDTO;
import com.microecommerce.productsservice.mappers.BrandMapper;
import com.microecommerce.productsservice.services.interfaces.IBrandService;
import com.microecommerce.utilitymodule.exceptions.DuplicatedRelationException;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;
import com.microecommerce.utilitymodule.exceptions.RelatedEntityNotFoundException;
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
        return BrandMapper.fromEntities(brandService.getAll());
    }

    @GetMapping("/{id}")
    public BrandDTO getBrandById(@PathVariable Long id) throws EntityNotFoundException {
        return BrandMapper.fromEntity(brandService.getById(id));
    }

    @PostMapping
    public BrandDTO addBrand(@RequestBody BrandDTO brand) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        return addBrands(Collections.singletonList(brand)).get(0);
    }

    @PostMapping("/batch")
    public List<BrandDTO> addBrands(@RequestBody List<BrandDTO> brands) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        return BrandMapper.fromEntities(brandService.createBatch(BrandMapper.toEntities(brands)));
    }

    @PutMapping("/{id}")
    public BrandDTO updateBrand(@PathVariable Long id, @RequestBody BrandDTO brand) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException, InvalidEntityException {
        brand.setId(id);
        return updateBrands(Collections.singletonList(brand)).get(0);
    }

    @PutMapping("/batch")
    public List<BrandDTO> updateBrands(@RequestBody List<BrandDTO> brands) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException, InvalidEntityException {
        return BrandMapper.fromEntities(brandService.updateBatch(BrandMapper.toEntities(brands)));
    }

    @DeleteMapping("/{id}")
    public void deleteBrand(@PathVariable Long id) throws EntityNotFoundException {
        brandService.deleteById(id);
    }
}

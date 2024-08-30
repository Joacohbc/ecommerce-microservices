package com.microecommerce.productsservice.services;

import com.microecommerce.productsservice.models.Brand;
import com.microecommerce.productsservice.repositories.BrandRepository;
import com.microecommerce.productsservice.services.interfaces.IBrandService;
import com.microecommerce.utilitymodule.exceptions.DuplicatedRelationException;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;
import com.microecommerce.utilitymodule.exceptions.RelatedEntityNotFoundException;
import com.microecommerce.utilitymodule.interfaces.IGetId;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService implements IBrandService {
    private final BrandRepository brandRepository;

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
        return brandRepository.findAllById(ids);
    }

    @Override
    public Brand create(@Valid Brand entity) throws RelatedEntityNotFoundException, DuplicatedRelationException {
        return createBatch(Collections.singletonList(entity)).get(0);
    }

    @Override
    public List<Brand> createBatch(@Valid List<Brand> entities) throws RelatedEntityNotFoundException, DuplicatedRelationException {
        if(entities.isEmpty()) return Collections.emptyList();
        entities.forEach(brand -> brand.setId(null));
        validateDuplicatedNames(entities);
        return brandRepository.saveAll(entities);
    }

    @Override
    public Brand update(@Valid Brand entity) throws RelatedEntityNotFoundException, DuplicatedRelationException, InvalidEntityException {
        return updateBatch(Collections.singletonList(entity)).get(0);
    }

    @Override
    public List<Brand> updateBatch(@Valid List<Brand> entities) throws RelatedEntityNotFoundException, DuplicatedRelationException, InvalidEntityException {
        if(entities.isEmpty()) return Collections.emptyList();
        if(!IGetId.allHaveId(entities)) throw new InvalidEntityException("All brands must have an ID to be updated");
        validateDuplicatedNames(entities);

        return brandRepository.saveAll(entities);
    }

    @Override
    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }

    private void validateDuplicatedNames(List<Brand> brands) throws RelatedEntityNotFoundException, DuplicatedRelationException {
        var names = brands.stream().map(Brand::getName).collect(Collectors.toList());
        var repeatedName = brandRepository.existsByNameIn(names);
        if(repeatedName) throw new RelatedEntityNotFoundException("Some brands have repeated name at database");

        // Check if there are repeated Name in the request (only if there are more than one product)
        if(!brands.isEmpty() && (brands.size() != new HashSet<>(names).size()))
            throw new DuplicatedRelationException("Some brands have repeated name in the request");
    }
}

package com.microecommerce.productsservice.controllers;

import com.microecommerce.productsservice.dtos.DetailDTO;
import com.microecommerce.productsservice.exceptions.DuplicatedRelationException;
import com.microecommerce.productsservice.exceptions.EntityNotFoundException;
import com.microecommerce.productsservice.exceptions.InvalidEntityException;
import com.microecommerce.productsservice.exceptions.RelatedEntityNotFoundException;
import com.microecommerce.productsservice.services.interfaces.IDetailService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/details")
public class DetailController {

    private final IDetailService detailService;

    public DetailController(IDetailService detailService) {
        this.detailService = detailService;
    }

    @GetMapping
    public List<DetailDTO> getAllDetails() {
        return DetailDTO.fromEntities(detailService.getAll());
    }

    @GetMapping("/{id}")
    public DetailDTO getDetailById(@PathVariable Long id) throws EntityNotFoundException {
        return DetailDTO.fromEntity(detailService.getById(id));
    }

    @PostMapping
    public DetailDTO addDetail(@RequestBody DetailDTO detail) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        return addDetails(Collections.singletonList(detail)).get(0);
    }

    @PostMapping("/batch")
    public List<DetailDTO> addDetails(@RequestBody List<DetailDTO> details) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        return DetailDTO.fromEntities(detailService.createBatch(DetailDTO.toEntities(details)));
    }

    @PutMapping("/{id}")
    public DetailDTO updateDetail(@PathVariable Long id, @RequestBody DetailDTO detail) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException, InvalidEntityException {
        detail.setId(id);
        return updateDetails(Collections.singletonList(detail)).get(0);
    }

    @PutMapping("/batch")
    public List<DetailDTO> updateDetails(@RequestBody List<DetailDTO> details) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException, InvalidEntityException {
        return DetailDTO.fromEntities(detailService.updateBatch(DetailDTO.toEntities(details)));
    }

    @DeleteMapping("/{id}")
    public void deleteDetail(@PathVariable Long id) throws EntityNotFoundException {
        detailService.deleteById(id);
    }
}

package com.microecommerce.productsservice.controllers;

import com.microecommerce.dtoslibrary.products_service.DetailDTO;
import com.microecommerce.productsservice.mappers.DetailMapper;
import com.microecommerce.productsservice.services.interfaces.IDetailService;
import com.microecommerce.utilitymodule.exceptions.DuplicatedRelationException;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;
import com.microecommerce.utilitymodule.exceptions.RelatedEntityNotFoundException;
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
        return DetailMapper.fromEntities(detailService.getAll());
    }

    @GetMapping("/{id}")
    public DetailDTO getDetailById(@PathVariable Long id) throws EntityNotFoundException {
        return DetailMapper.fromEntity(detailService.getById(id));
    }

    @PostMapping
    public DetailDTO addDetail(@RequestBody DetailDTO detail) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        return addDetails(Collections.singletonList(detail)).get(0);
    }

    @PostMapping("/batch")
    public List<DetailDTO> addDetails(@RequestBody List<DetailDTO> details) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        return DetailMapper.fromEntities(detailService.createBatch(DetailMapper.toEntities(details)));
    }

    @PutMapping("/{id}")
    public DetailDTO updateDetail(@PathVariable Long id, @RequestBody DetailDTO detail) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException, InvalidEntityException {
        detail.setId(id);
        return updateDetails(Collections.singletonList(detail)).get(0);
    }

    @PutMapping("/batch")
    public List<DetailDTO> updateDetails(@RequestBody List<DetailDTO> details) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException, InvalidEntityException {
        return DetailMapper.fromEntities(detailService.updateBatch(DetailMapper.toEntities(details)));
    }

    @DeleteMapping("/{id}")
    public void deleteDetail(@PathVariable Long id) throws EntityNotFoundException {
        detailService.deleteById(id);
    }
}

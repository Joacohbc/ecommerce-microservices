package com.microecommerce.productsservice.controllers;

import com.microecommerce.productsservice.dtos.TagDTO;
import com.microecommerce.productsservice.exceptions.DuplicatedRelationException;
import com.microecommerce.productsservice.exceptions.EntityNotFoundException;
import com.microecommerce.productsservice.exceptions.InvalidEntityException;
import com.microecommerce.productsservice.exceptions.RelatedEntityNotFoundException;
import com.microecommerce.productsservice.services.interfaces.ITagService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final ITagService tagService;

    public TagController(ITagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagDTO> getAllTags() {
        return TagDTO.fromEntities(tagService.getAll());
    }

    @GetMapping("/{id}")
    public TagDTO getTagById(@PathVariable Long id) throws EntityNotFoundException {
        return TagDTO.fromEntity(tagService.getById(id));
    }

    @PostMapping
    public TagDTO addTag(@RequestBody TagDTO tag) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        return addTags(Collections.singletonList(tag)).get(0);
    }

    @PostMapping("/batch")
    public List<TagDTO> addTags(@RequestBody List<TagDTO> Tags) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        return TagDTO.fromEntities(tagService.createBatch(TagDTO.toEntities(Tags)));
    }

    @PutMapping("/{id}")
    public TagDTO updateTag(@PathVariable Long id, @RequestBody TagDTO tag) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException, InvalidEntityException {
        tag.setId(id);
        return updateTags(Collections.singletonList(tag)).get(0);
    }

    @PutMapping("/batch")
    public List<TagDTO> updateTags(@RequestBody List<TagDTO> Tags) throws EntityNotFoundException, DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        return TagDTO.fromEntities(tagService.updateBatch(TagDTO.toEntities(Tags)));
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Long id) throws EntityNotFoundException {
        tagService.deleteById(id);
    }
}

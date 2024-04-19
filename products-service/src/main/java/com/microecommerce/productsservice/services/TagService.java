package com.microecommerce.productsservice.services;

import com.microecommerce.productsservice.exceptions.EntityNotFoundException;
import com.microecommerce.productsservice.exceptions.InvalidEntityException;
import com.microecommerce.productsservice.models.IGetId;
import com.microecommerce.productsservice.models.Tag;
import com.microecommerce.productsservice.repositories.TagRepository;
import com.microecommerce.productsservice.services.interfaces.ITagService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService implements ITagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag getById(Long id) throws EntityNotFoundException {
        return tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found"));
    }

    @Override
    public List<Tag> getByIds(List<Long> ids) {
        return tagRepository.findAllById(ids);
    }

    @Override
    public Tag create(@Valid Tag entity) throws InvalidEntityException {
        return createBatch(Collections.singletonList(entity)).get(0);
    }

    @Override
    public List<Tag> createBatch(@Valid List<Tag> entities) throws InvalidEntityException {
        if(entities.isEmpty()) return Collections.emptyList();
        entities.forEach(tag -> tag.setId(null));
        validateDuplicatedNames(entities);
        return tagRepository.saveAll(entities);
    }

    @Override
    public Tag update(@Valid Tag entity) {
        return tagRepository.save(entity);
    }

    @Override
    public List<Tag> updateBatch(@Valid List<Tag> entities) throws InvalidEntityException {
        if(entities.isEmpty()) return Collections.emptyList();
        if(!IGetId.allHaveId(entities)) throw new InvalidEntityException("All tags must have an ID to be updated");
        validateDuplicatedNames(entities);
        return tagRepository.saveAll(entities);
    }

    @Override
    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }

    private void validateDuplicatedNames(List<Tag> tags) throws InvalidEntityException {
        var names = tags.stream().map(Tag::getName).collect(Collectors.toList());
        var repeatedName = tagRepository.existsByNameIn(names);
        if(repeatedName) throw new InvalidEntityException("Some tags have repeated name at database");

        // Check if there are repeated Name in the request (only if there are more than one product)
        if(!tags.isEmpty() && (tags.size() != new HashSet<>(names).size()))
            throw new InvalidEntityException("Some tags have repeated name in the request");
    }
}

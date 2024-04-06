package com.microecommerce.productsservice.services;

import com.microecommerce.productsservice.models.Tag;
import com.microecommerce.productsservice.repositories.TagRepository;
import com.microecommerce.productsservice.services.interfaces.ITagService;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Tag getById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    @Override
    public List<Tag> getByIds(List<Long> ids) {
        return tagRepository.findAllById(ids);
    }

    @Override
    public Tag create(Tag entity) {
        return tagRepository.save(entity);
    }

    @Override
    public List<Tag> createBatch(List<Tag> entities) {
        return tagRepository.saveAll(entities);
    }

    @Override
    public Tag update(Tag entity) {
        return tagRepository.save(entity);
    }

    @Override
    public List<Tag> updateBatch(List<Tag> entities) {
        return tagRepository.saveAll(entities);
    }

    @Override
    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }
}

package com.microecommerce.productsservice.services;

import com.microecommerce.productsservice.exceptions.NoRelatedEntityException;
import com.microecommerce.productsservice.models.Detail;
import com.microecommerce.productsservice.services.interfaces.IDetailService;
import com.microecommerce.productsservice.repositories.DetailRepository;

import java.util.List;

public class DetailService  implements IDetailService {

    private final DetailRepository detailRepository;

    public DetailService(DetailRepository detailRepository) {
        this.detailRepository = detailRepository;
    }

    @Override
    public List<Detail> getAll() {
        return detailRepository.findAll();
    }

    @Override
    public Detail getById(Long id) {
        return detailRepository.findById(id).orElse(null);
    }

    @Override
    public List<Detail> getByIds(List<Long> ids) {
        return detailRepository.findAllById(ids);
    }

    @Override
    public Detail create(Detail entity) throws NoRelatedEntityException {
        return detailRepository.save(entity);
    }

    @Override
    public List<Detail> createBatch(List<Detail> entities) throws NoRelatedEntityException {
        return detailRepository.saveAll(entities);
    }

    @Override
    public Detail update(Detail entity) {
        return detailRepository.save(entity);
    }

    @Override
    public List<Detail> updateBatch(List<Detail> entities) {
        return detailRepository.saveAll(entities);
    }

    @Override
    public void deleteById(Long id) {
        detailRepository.deleteById(id);
    }
}

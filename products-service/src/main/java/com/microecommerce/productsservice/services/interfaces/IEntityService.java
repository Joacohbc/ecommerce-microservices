package com.microecommerce.productsservice.services.interfaces;

import com.microecommerce.productsservice.exceptions.DuplicatedRelationException;
import com.microecommerce.productsservice.exceptions.EntityNotFoundException;
import com.microecommerce.productsservice.exceptions.NoRelatedEntityException;

import java.util.List;

public interface IEntityService<T> {
    List<T> getAll();
    T getById(Long id) throws EntityNotFoundException;
    List<T> getByIds(List<Long> ids);
    T create(T entity) throws NoRelatedEntityException, DuplicatedRelationException;
    List<T> createBatch(List<T> entities) throws NoRelatedEntityException, DuplicatedRelationException;
    T update(T entity);
    List<T> updateBatch(List<T> entities);
    void deleteById(Long id) throws EntityNotFoundException;
}

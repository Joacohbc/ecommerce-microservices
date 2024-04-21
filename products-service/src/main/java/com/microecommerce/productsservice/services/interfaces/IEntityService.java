package com.microecommerce.productsservice.services.interfaces;

import com.microecommerce.utilitymodule.exceptions.DuplicatedRelationException;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;
import com.microecommerce.utilitymodule.exceptions.RelatedEntityNotFoundException;

import java.util.List;

public interface IEntityService<T> {
    List<T> getAll();
    T getById(Long id) throws EntityNotFoundException;
    List<T> getByIds(List<Long> ids);

    T create(T entity) throws InvalidEntityException, RelatedEntityNotFoundException, DuplicatedRelationException;
    List<T> createBatch(List<T> entities) throws InvalidEntityException, RelatedEntityNotFoundException, DuplicatedRelationException;

    T update(T entity) throws InvalidEntityException, EntityNotFoundException, RelatedEntityNotFoundException, DuplicatedRelationException;
    List<T> updateBatch(List<T> entities) throws InvalidEntityException, EntityNotFoundException, RelatedEntityNotFoundException, DuplicatedRelationException;

    void deleteById(Long id) throws EntityNotFoundException;
}

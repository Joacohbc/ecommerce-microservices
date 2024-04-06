package com.microecommerce.productsservice.services.interfaces;

import com.microecommerce.productsservice.exceptions.NoRelatedEntityException;
import com.microecommerce.productsservice.models.Product;

import java.util.List;

public interface IEntityService<T> {
    public List<T> getAll();
    public T getById(Long id);
    public List<T> getByIds(List<Long> ids);
    public T create(T entity) throws NoRelatedEntityException;
    public List<T> createBatch(List<T> entities) throws NoRelatedEntityException;
    public T update(T entity);
    public List<T> updateBatch(List<T> entities);
    public void deleteById(Long id);
}

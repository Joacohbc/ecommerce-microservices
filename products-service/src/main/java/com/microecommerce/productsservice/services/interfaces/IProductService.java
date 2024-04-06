package com.microecommerce.productsservice.services.interfaces;
import com.microecommerce.productsservice.exceptions.NoRelatedEntityException;
import  com.microecommerce.productsservice.models.Product;

import java.util.List;
import java.util.Map;

public interface IProductService extends IEntityService<Product> {
    Product addTag(Long productId, Long tagId) throws NoRelatedEntityException;
    Product removeTag(Long productId, Long tagId) throws NoRelatedEntityException;
    Product addCategory(Long productId, Long categoryId) throws NoRelatedEntityException;
    Product removeCategory(Long productId, Long categoryId) throws NoRelatedEntityException;
    Product addDetails(Long productId, Map<Long, Object> details) throws NoRelatedEntityException;
    Product removeDetails(Long productId, List<Long> detailIds) throws NoRelatedEntityException;
}

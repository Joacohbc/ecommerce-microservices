package com.microecommerce.productsservice.services.interfaces;
import com.microecommerce.productsservice.exceptions.EntityNotFoundException;
import com.microecommerce.productsservice.exceptions.NoRelatedEntityException;
import  com.microecommerce.productsservice.models.Product;
import com.microecommerce.productsservice.models.ProductDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface IProductService extends IEntityService<Product> {
    Product create(Product entity) throws NoRelatedEntityException;
    List<Product> createBatch(List<Product> entities) throws NoRelatedEntityException;
    Product addTag(Long productId, Long tagId) throws EntityNotFoundException;
    Product removeTag(Long productId, Long tagId) throws EntityNotFoundException;
    Product addCategory(Long productId, Long categoryId) throws EntityNotFoundException;
    Product removeCategory(Long productId, Long categoryId) throws EntityNotFoundException;
    Product addDetails(Long productId, List<ProductDetails> details) throws NoRelatedEntityException, EntityNotFoundException;
    Product removeDetails(Long productId, List<Long> detailIds) throws NoRelatedEntityException, EntityNotFoundException;
    Page<Product> getAllByPage(int page, int size, Product.ProductFields orderBy, Sort.Direction direction);
}

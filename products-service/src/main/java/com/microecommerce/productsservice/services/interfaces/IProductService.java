package com.microecommerce.productsservice.services.interfaces;
import com.microecommerce.productsservice.exceptions.NoRelatedEntityException;
import  com.microecommerce.productsservice.models.Product;
import com.microecommerce.productsservice.models.ProductDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface IProductService extends IEntityService<Product> {
    Product addTag(Long productId, Long tagId) throws NoRelatedEntityException;
    Product removeTag(Long productId, Long tagId) throws NoRelatedEntityException;
    Product addCategory(Long productId, Long categoryId) throws NoRelatedEntityException;
    Product removeCategory(Long productId, Long categoryId) throws NoRelatedEntityException;
    Product addDetails(Long productId, List<ProductDetails> details) throws NoRelatedEntityException;
    Product removeDetails(Long productId, List<Long> detailIds) throws NoRelatedEntityException;
    Page<Product> getAllByPage(int page, int size, Product.ProductFields orderBy, Sort.Direction direction);
}

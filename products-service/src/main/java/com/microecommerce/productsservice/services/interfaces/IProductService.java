package com.microecommerce.productsservice.services.interfaces;

import com.microecommerce.productsservice.models.Product;
import com.microecommerce.productsservice.models.ProductDetails;
import com.microecommerce.utilitymodule.exceptions.DuplicatedRelationException;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.RelatedEntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface IProductService extends IEntityService<Product> {
    Product addTag(Long productId, Long tagId) throws EntityNotFoundException, RelatedEntityNotFoundException, DuplicatedRelationException;
    Product removeTag(Long productId, Long tagId) throws EntityNotFoundException, RelatedEntityNotFoundException, DuplicatedRelationException;
    Product addCategory(Long productId, Long categoryId) throws EntityNotFoundException, RelatedEntityNotFoundException, DuplicatedRelationException;
    Product removeCategory(Long productId, Long categoryId) throws EntityNotFoundException, RelatedEntityNotFoundException, DuplicatedRelationException;
    Product addDetails(Long productId, List<ProductDetails> details) throws EntityNotFoundException, RelatedEntityNotFoundException, DuplicatedRelationException;
    Product removeDetails(Long productId, List<Long> detailIds) throws EntityNotFoundException, RelatedEntityNotFoundException, DuplicatedRelationException;
    Product updateDetail(Long productId, Long detailId, ProductDetails value) throws EntityNotFoundException, RelatedEntityNotFoundException;
    Page<Product> getAllByPage(int page, int size, Product.ProductFields orderBy, Sort.Direction direction);
    boolean checkProductsExistence(List<Long> productIds);
}

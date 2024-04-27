package com.microecommerce.productsservice.repositories;


import com.microecommerce.productsservice.models.Brand;
import com.microecommerce.productsservice.models.Category;
import com.microecommerce.productsservice.models.Product;
import com.microecommerce.productsservice.models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;

public interface ProductRepository extends JpaRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {

    Page<Product> findByCategoriesAndNameContainingIgnoreCase(Collection<Category> categories, String name, Pageable pageable);
    Collection<Product> findByBrandsAndNameContainingIgnoreCase(Collection<Brand> brands, String name);
    Collection<Product> findByTagsAndNameContainingIgnoreCase(Collection<Tag> tags, String name);
    Collection<Product> findByCategoriesAndBrandsAndTagsAndNameContainingIgnoreCase(Collection<Category> categories, Collection<Brand> brands, Collection<Tag> tags, String name);
    Collection<Product> findByCategoriesOrBrandsOrTagsAndNameContainingIgnoreCase(Collection<Category> categories, Collection<Brand> brands, Collection<Tag> tags, String name);
    Long countByCategoriesAndNameContainingIgnoreCase(Collection<Category> categories, String name);
    Long countByBrandsAndNameContainingIgnoreCase(Collection<Brand> brands, String name);
    Long countByTagsAndNameContainingIgnoreCase(Collection<Tag> tags, String name);
    Long countByCategoriesAndBrandsAndTagsAndNameContainingIgnoreCase(Collection<Category> categories, Collection<Brand> brands, Collection<Tag> tags, String name);
    Long countByCategoriesOrBrandsOrTagsAndNameContainingIgnoreCase(Collection<Category> categories, Collection<Brand> brands, Collection<Tag> tags, String name);
    Collection<Product> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    boolean existsProductBySkuIn(Collection<String> skus);
    boolean existsProductsByIdIn(Collection<Long> ids);
}

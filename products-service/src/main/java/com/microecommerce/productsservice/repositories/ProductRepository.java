package com.microecommerce.productsservice.repositories;


import com.microecommerce.productsservice.models.Brand;
import com.microecommerce.productsservice.models.Category;
import com.microecommerce.productsservice.models.Product;
import com.microecommerce.productsservice.models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {

    Page<Product> findByCategoriesAndNameContainingIgnoreCase(List<Category> categories, String name, Pageable pageable);
    List<Product> findByBrandsAndNameContainingIgnoreCase(List<Brand> brands, String name);
    List<Product> findByTagsAndNameContainingIgnoreCase(List<Tag> tags, String name);
    List<Product> findByCategoriesAndBrandsAndTagsAndNameContainingIgnoreCase(List<Category> categories, List<Brand> brands, List<Tag> tags, String name);
    List<Product> findByCategoriesOrBrandsOrTagsAndNameContainingIgnoreCase(List<Category> categories, List<Brand> brands, List<Tag> tags, String name);
    Long countByCategoriesAndNameContainingIgnoreCase(List<Category> categories, String name);
    Long countByBrandsAndNameContainingIgnoreCase(List<Brand> brands, String name);
    Long countByTagsAndNameContainingIgnoreCase(List<Tag> tags, String name);
    Long countByCategoriesAndBrandsAndTagsAndNameContainingIgnoreCase(List<Category> categories, List<Brand> brands, List<Tag> tags, String name);
    Long countByCategoriesOrBrandsOrTagsAndNameContainingIgnoreCase(List<Category> categories, List<Brand> brands, List<Tag> tags, String name);
    List<Product> findByNameContainingIgnoreCaseOrderByNameAsc(String name);
    List<Product> findAllByIdIn(List<Long> ids);
    boolean existsProductBySkuIn(List<String> skus);
}

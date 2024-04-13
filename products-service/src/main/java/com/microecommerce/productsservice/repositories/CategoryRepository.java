package com.microecommerce.productsservice.repositories;

import com.microecommerce.productsservice.models.Brand;
import com.microecommerce.productsservice.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository  extends JpaRepository<Category, Long> {
    boolean existsByNameIn(List<String> names);
}

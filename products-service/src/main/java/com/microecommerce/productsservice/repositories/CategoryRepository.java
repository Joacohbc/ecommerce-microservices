package com.microecommerce.productsservice.repositories;

import com.microecommerce.productsservice.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface CategoryRepository  extends JpaRepository<Category, Long> {
    boolean existsByNameIn(Collection<String> names);
}

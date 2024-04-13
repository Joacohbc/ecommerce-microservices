package com.microecommerce.productsservice.repositories;

import com.microecommerce.productsservice.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByNameIn(List<String> names);
}

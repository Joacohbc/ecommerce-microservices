package com.microecommerce.productsservice.repositories;

import com.microecommerce.productsservice.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByNameIn(Collection<String> names);
}

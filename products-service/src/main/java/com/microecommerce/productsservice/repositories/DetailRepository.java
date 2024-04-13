package com.microecommerce.productsservice.repositories;

import com.microecommerce.productsservice.models.Detail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface DetailRepository extends JpaRepository<Detail, Long> {
    boolean existsByNameIn(Collection<String> names);
}

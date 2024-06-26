package com.microecommerce.productsservice.repositories;

import com.microecommerce.productsservice.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByNameIn(Collection<String> names);
}

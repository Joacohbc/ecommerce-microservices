package com.microecommerce.productsservice.repositories;

import com.microecommerce.productsservice.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    public List<Tag> findAllByIdIn(List<Long> ids);
}

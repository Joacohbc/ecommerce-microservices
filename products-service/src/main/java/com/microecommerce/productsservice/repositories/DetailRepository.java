package com.microecommerce.productsservice.repositories;

import com.microecommerce.productsservice.models.Detail;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.lang.model.element.Name;
import java.util.List;

public interface DetailRepository extends JpaRepository<Detail, Long> {
    boolean existsByNameIn(List<String> names);
}

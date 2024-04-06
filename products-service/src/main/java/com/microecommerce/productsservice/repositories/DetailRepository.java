package com.microecommerce.productsservice.repositories;

import com.microecommerce.productsservice.models.Detail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailRepository extends JpaRepository<Detail, Long> {
}

package com.microecommerce.utilitymodule.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomJPARepository<T, R> extends JpaRepository<T, R>, PagingAndSortingRepository<T, R> {
}

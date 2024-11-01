package com.microecommerce.productsservice.repositories;

import com.microecommerce.productsservice.models.Detail;
import com.microecommerce.productsservice.models.Product;
import com.microecommerce.productsservice.models.ProductDetails;
import com.microecommerce.productsservice.models.ProductDetailsKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, ProductDetailsKey> {
    ProductDetails findByProductAndDetail(Product product, Detail detail);
}

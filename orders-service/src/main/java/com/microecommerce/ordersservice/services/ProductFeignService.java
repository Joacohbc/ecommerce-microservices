package com.microecommerce.ordersservice.services;

import com.microecommerce.dtoslibrary.products_service.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "products-service")
public interface ProductFeignService {

    @GetMapping("/products/{id}")
    ProductDTO getProductById(Long id);

    @GetMapping("/products")
    List<ProductDTO> getAllProducts();

    @GetMapping("/products/check-existence")
    boolean checkProductsExistence(@RequestParam List<Long> productIds);
}

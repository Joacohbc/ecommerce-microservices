package com.microecommerce.productsservice.controllers;

import com.microecommerce.productsservice.models.Product;
import com.microecommerce.productsservice.services.ProductService;
import com.microecommerce.productsservice.services.interfaces.IProductService;
import com.microecommerce.productsservice.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Object> addProduct(@RequestBody Product product) {
        try {
            return ResponseEntity.ok().body(productService.create(product));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(JSONUtils.createResponse(e.getMessage()));
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<Object> addProducts(@RequestBody List<Product> products) {
        try {
            return ResponseEntity.ok().body(productService.createBatch(products));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(JSONUtils.createResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return productService.update(product);
    }

    @PutMapping("/batch")
    public List<Product> updateProducts(@RequestBody List<Product> products) {
        return productService.updateBatch(products);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
    }
}

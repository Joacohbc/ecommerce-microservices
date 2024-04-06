package com.microecommerce.productsservice.controllers;

import com.microecommerce.productsservice.exceptions.NoRelatedEntityException;
import com.microecommerce.productsservice.models.Product;
import com.microecommerce.productsservice.models.ProductDetails;
import com.microecommerce.productsservice.services.interfaces.IProductService;
import com.microecommerce.productsservice.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

    @PutMapping("/{id}/tags/{tagId}")
    public ResponseEntity<Object> addTag(@PathVariable Long id, @PathVariable Long tagId) {
        try {
            return ResponseEntity.ok().body(productService.addTag(id, tagId));
        } catch (NoRelatedEntityException e) {
            return ResponseEntity.badRequest().body(JSONUtils.createResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}/categories/{categoryId}")
    public Product addCategory(@PathVariable Long id, @PathVariable Long categoryId) {
        try {
            return productService.addCategory(id, categoryId);
        } catch (NoRelatedEntityException e) {
            return null;
        }
    }

    @PutMapping("/{id}/details")
    public ResponseEntity<Object> addDetails(@PathVariable Long id, @RequestBody Map<Long, Object> details) {
        try {

            return ResponseEntity.ok().body(productService.addDetails(id, details));
        } catch (NoRelatedEntityException e) {
            return ResponseEntity.badRequest().body(JSONUtils.createResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/details")
    public ResponseEntity<Object> removeDetails(@PathVariable Long id, @RequestBody List<Long> detailIds) {
        try {
            return ResponseEntity.ok().body(productService.removeDetails(id, detailIds));
        } catch (NoRelatedEntityException e) {
            return ResponseEntity.badRequest().body(JSONUtils.createResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/tags/{tagId}")
    public ResponseEntity<Object> removeTag(@PathVariable Long id, @PathVariable Long tagId) {
        try {
            return ResponseEntity.ok().body(productService.removeTag(id, tagId));
        } catch (NoRelatedEntityException e) {
            return ResponseEntity.badRequest().body(JSONUtils.createResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/categories/{categoryId}")
    public ResponseEntity<Object> removeCategory(@PathVariable Long id, @PathVariable Long categoryId) {
        try {
            return ResponseEntity.ok().body(productService.removeCategory(id, categoryId));
        } catch (NoRelatedEntityException e) {
            return ResponseEntity.badRequest().body(JSONUtils.createResponse(e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
    }
}

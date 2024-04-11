package com.microecommerce.productsservice.controllers;

import com.microecommerce.productsservice.dtos.ProductDTO;
import com.microecommerce.productsservice.dtos.ProductDetailsDTO;
import com.microecommerce.productsservice.models.Product;
import com.microecommerce.productsservice.services.interfaces.IProductService;
import com.microecommerce.productsservice.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return ProductDTO.fromEntities(productService.getAll());
    }

    @GetMapping(params = {"page", "sizePerPage", "sortField", "sortDirection"})
    public Page<ProductDTO> getProducts(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "2") int sizePerPage,
                                        @RequestParam(defaultValue = "name") String sortField,
                                        @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection) {

        var sortFieldEnum = Product.ProductFields.valueOf(sortField);
        var pageRequest = productService.getAllByPage(page, sizePerPage, sortFieldEnum, sortDirection);
        return pageRequest.map(ProductDTO::fromEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(ProductDTO.fromEntity(productService.getById(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(JSONUtils.createResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Object> addProduct(@RequestBody ProductDTO product) {
        return addProducts(List.of(product));
    }

    @PostMapping("/batch")
    public ResponseEntity<Object> addProducts(@RequestBody List<ProductDTO> products) {
        try {
            var entities = ProductDTO.toEntities(products);
            var created = ProductDTO.fromEntities(productService.createBatch(entities));
            return ResponseEntity.ok().body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(JSONUtils.createResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductDTO product) {
        return updateProducts(List.of(product)).get(0);
    }

    @PutMapping("/batch")
    public List<Product> updateProducts(@RequestBody List<ProductDTO> products) {
        var entities = ProductDTO.toEntities(products);
        return productService.updateBatch(entities);
    }

    @PostMapping("/{id}/tags/{tagId}")
    public ResponseEntity<Object> addTag(@PathVariable Long id, @PathVariable Long tagId) {
        try {
            return ResponseEntity.ok().body(productService.addTag(id, tagId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(JSONUtils.createResponse(e.getMessage()));
        }
    }

    @PostMapping("/{id}/categories/{categoryId}")
    public Product addCategory(@PathVariable Long id, @PathVariable Long categoryId) {
        try {
            return productService.addCategory(id, categoryId);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/{id}/details")
    public ResponseEntity<Object> addDetails(@PathVariable Long id, @RequestBody List<ProductDetailsDTO> details) {
        try {
            var entities = ProductDetailsDTO.toEntities(details, id);
            var added = productService.addDetails(id, entities);
            return ResponseEntity.ok().body(added);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(JSONUtils.createResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/details")
    public ResponseEntity<Object> removeDetails(@PathVariable Long id, @RequestBody List<Long> detailIds) {
        try {

            return ResponseEntity.ok().body(productService.removeDetails(id, detailIds));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(JSONUtils.createResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/tags/{tagId}")
    public ResponseEntity<Object> removeTag(@PathVariable Long id, @PathVariable Long tagId) {
        try {
            return ResponseEntity.ok().body(productService.removeTag(id, tagId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(JSONUtils.createResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/categories/{categoryId}")
    public ResponseEntity<Object> removeCategory(@PathVariable Long id, @PathVariable Long categoryId) {
        try {
            return ResponseEntity.ok().body(productService.removeCategory(id, categoryId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(JSONUtils.createResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(JSONUtils.createResponse(e.getMessage()));
        }
    }
}

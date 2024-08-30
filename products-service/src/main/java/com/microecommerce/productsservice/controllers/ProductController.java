package com.microecommerce.productsservice.controllers;

import com.microecommerce.dtoslibrary.products_service.ProductDTO;
import com.microecommerce.dtoslibrary.products_service.ProductDetailsDTO;
import com.microecommerce.productsservice.mappers.ProductDetailsMapper;
import com.microecommerce.productsservice.mappers.ProductMapper;
import com.microecommerce.productsservice.models.Product;
import com.microecommerce.productsservice.services.interfaces.IProductService;
import com.microecommerce.utilitymodule.exceptions.DuplicatedRelationException;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;
import com.microecommerce.utilitymodule.exceptions.RelatedEntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final IProductService productService;
    
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return ProductMapper.fromEntities(productService.getAll());
    }

    @GetMapping(params = { "page", "sizePerPage", "sortField", "sortDirection" })
    public Page<ProductDTO> getProducts(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "2") int sizePerPage,
                                        @RequestParam(defaultValue = "name") String sortField,
                                        @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection) {

        var sortFieldEnum = Product.ProductFields.valueOf(sortField);
        var pageRequest = productService.getAllByPage(page, sizePerPage, sortFieldEnum, sortDirection);
        return pageRequest.map(ProductMapper::fromEntity);
    }

    @GetMapping("/check-existence")
    public boolean checkProductsExistence(@RequestParam List<Long> productIds) {
        return productService.checkProductsExistence(productIds);
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) throws EntityNotFoundException {
        return ProductMapper.fromEntity(productService.getById(id));
    }

    @PostMapping
    public ProductDTO addProduct(@RequestBody ProductDTO product) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        return addProducts(List.of(product)).get(0);
    }

    @PostMapping("/batch")
    public List<ProductDTO> addProducts(@RequestBody List<ProductDTO> products) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        var entities = ProductMapper.toEntities(products);
        return ProductMapper.fromEntities(productService.createBatch(entities));
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductDTO product) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException, InvalidEntityException {
        product.setId(id);
        return ProductMapper.fromEntity(productService.update(ProductMapper.toEntity(product)));
    }

    @PutMapping("/batch")
    public List<ProductDTO> updateProducts(@RequestBody List<ProductDTO> products) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException, InvalidEntityException {
        var entities = ProductMapper.toEntities(products);
        return ProductMapper.fromEntities(productService.updateBatch(entities));
    }

    @PostMapping("/{id}/tags/{tagId}")
    public ProductDTO addTag(@PathVariable Long id, @PathVariable Long tagId) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException {
        return ProductMapper.fromEntity(productService.addTag(id, tagId));
    }

    @PostMapping("/{id}/categories/{categoryId}")
    public ProductDTO addCategory(@PathVariable Long id, @PathVariable Long categoryId) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException {
        return ProductMapper.fromEntity(productService.addCategory(id, categoryId));
    }

    @PostMapping("/{id}/details")
    public ProductDTO addDetails(@PathVariable Long id, @RequestBody List<ProductDetailsDTO> details) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException {
        var entities = ProductDetailsMapper.toEntities(details, id);
        return ProductMapper.fromEntity(productService.addDetails(id, entities));
    }

    @DeleteMapping("/{id}/details")
    public ProductDTO removeDetails(@PathVariable Long id, @RequestBody List<Long> detailIds) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException {
        return ProductMapper.fromEntity(productService.removeDetails(id, detailIds));
    }

    @PutMapping("/{id}/details/{detailId}")
    public ProductDTO updateDetailValue(@PathVariable Long id, @PathVariable Long detailId, @RequestBody ProductDetailsDTO value) throws EntityNotFoundException, RelatedEntityNotFoundException {
        return ProductMapper.fromEntity(productService.updateDetail(id, detailId, ProductDetailsMapper.toEntity(value, id)));
    }

    @DeleteMapping("/{id}/tags/{tagId}")
    public ProductDTO removeTag(@PathVariable Long id, @PathVariable Long tagId) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException {
        return ProductMapper.fromEntity(productService.removeTag(id, tagId));
    }

    @DeleteMapping("/{id}/categories/{categoryId}")
    public ProductDTO removeCategory(@PathVariable Long id, @PathVariable Long categoryId) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException {
        return ProductMapper.fromEntity(productService.removeCategory(id, categoryId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) throws EntityNotFoundException {
        productService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

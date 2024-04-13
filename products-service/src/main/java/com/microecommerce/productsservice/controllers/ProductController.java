package com.microecommerce.productsservice.controllers;

import com.microecommerce.productsservice.dtos.ProductDTO;
import com.microecommerce.productsservice.dtos.ProductDetailsDTO;
import com.microecommerce.productsservice.exceptions.DuplicatedRelationException;
import com.microecommerce.productsservice.exceptions.EntityNotFoundException;
import com.microecommerce.productsservice.exceptions.InvalidEntityException;
import com.microecommerce.productsservice.exceptions.RelatedEntityNotFoundException;
import com.microecommerce.productsservice.models.Product;
import com.microecommerce.productsservice.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(params = { "page", "sizePerPage", "sortField", "sortDirection" })
    public Page<ProductDTO> getProducts(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "2") int sizePerPage,
                                        @RequestParam(defaultValue = "name") String sortField,
                                        @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection) {

        var sortFieldEnum = Product.ProductFields.valueOf(sortField);
        var pageRequest = productService.getAllByPage(page, sizePerPage, sortFieldEnum, sortDirection);
        return pageRequest.map(ProductDTO::fromEntity);
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) throws EntityNotFoundException {
        return ProductDTO.fromEntity(productService.getById(id));
    }

    @PostMapping
    public ProductDTO addProduct(@RequestBody ProductDTO product) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        return addProducts(List.of(product)).get(0);
    }

    @PostMapping("/batch")
    public List<ProductDTO> addProducts(@RequestBody List<ProductDTO> products) throws DuplicatedRelationException, RelatedEntityNotFoundException, InvalidEntityException {
        var entities = ProductDTO.toEntities(products);
        return ProductDTO.fromEntities(productService.createBatch(entities));
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductDTO product) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException, InvalidEntityException {
        product.setId(id);
        return ProductDTO.fromEntity(productService.update(ProductDTO.toEntity(product)));
    }

    @PutMapping("/batch")
    public List<ProductDTO> updateProducts(@RequestBody List<ProductDTO> products) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException, InvalidEntityException {
        var entities = ProductDTO.toEntities(products);
        return ProductDTO.fromEntities(productService.updateBatch(entities));
    }

    @PostMapping("/{id}/tags/{tagId}")
    public ProductDTO addTag(@PathVariable Long id, @PathVariable Long tagId) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException {
        return ProductDTO.fromEntity(productService.addTag(id, tagId));
    }

    @PostMapping("/{id}/categories/{categoryId}")
    public ProductDTO addCategory(@PathVariable Long id, @PathVariable Long categoryId) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException {
        return ProductDTO.fromEntity(productService.addCategory(id, categoryId));
    }

    @PostMapping("/{id}/details")
    public ProductDTO addDetails(@PathVariable Long id, @RequestBody List<ProductDetailsDTO> details) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException {
        var entities = ProductDetailsDTO.toEntities(details, id);
        return ProductDTO.fromEntity(productService.addDetails(id, entities));
    }

    @DeleteMapping("/{id}/details")
    public ProductDTO removeDetails(@PathVariable Long id, @RequestBody List<Long> detailIds) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException {
        return ProductDTO.fromEntity(productService.removeDetails(id, detailIds));
    }

    @PutMapping("/{id}/details/{detailId}")
    public ProductDTO updateDetailValue(@PathVariable Long id, @PathVariable Long detailId, @RequestBody ProductDetailsDTO value) throws EntityNotFoundException, RelatedEntityNotFoundException {
        return ProductDTO.fromEntity(productService.updateDetail(id, detailId, ProductDetailsDTO.toEntity(value, id)));
    }

    @DeleteMapping("/{id}/tags/{tagId}")
    public ProductDTO removeTag(@PathVariable Long id, @PathVariable Long tagId) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException {
        return ProductDTO.fromEntity(productService.removeTag(id, tagId));
    }

    @DeleteMapping("/{id}/categories/{categoryId}")
    public ProductDTO removeCategory(@PathVariable Long id, @PathVariable Long categoryId) throws DuplicatedRelationException, RelatedEntityNotFoundException, EntityNotFoundException {
        return ProductDTO.fromEntity(productService.removeCategory(id, categoryId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) throws EntityNotFoundException {
        productService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

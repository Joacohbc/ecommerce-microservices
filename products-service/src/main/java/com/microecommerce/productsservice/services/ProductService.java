package com.microecommerce.productsservice.services;

import com.microecommerce.productsservice.exceptions.NoRelatedEntityException;
import com.microecommerce.productsservice.models.Brand;
import com.microecommerce.productsservice.models.Category;
import com.microecommerce.productsservice.models.Product;
import com.microecommerce.productsservice.models.Tag;
import com.microecommerce.productsservice.repositories.ProductRepository;
import com.microecommerce.productsservice.services.interfaces.IBrandService;
import com.microecommerce.productsservice.services.interfaces.ICategoryService;
import com.microecommerce.productsservice.services.interfaces.IProductService;
import com.microecommerce.productsservice.services.interfaces.ITagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final ICategoryService categoryService;
    private final IBrandService brandService;
    private final ITagService tagService;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    public ProductService(ProductRepository productRepository, ICategoryService categoryService, IBrandService brandService, ITagService tagService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.tagService = tagService;
    }

    @Override
    public List<Product> getAll() {
        Pageable sortedByName = PageRequest.of(5, 50, Sort.by("name"));

        productRepository.findAll(sortedByName);
        return productRepository.findAll();
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> getByIds(List<Long> ids) {
        return productRepository.findAllById(ids);
    }

    @Override
    public Product create(Product product) throws NoRelatedEntityException {
        return createBatch(List.of(product)).get(0);
    }

    @Override
    public List<Product> createBatch(List<Product> products) throws NoRelatedEntityException {
        if (products.isEmpty()) {
            return List.of();
        }

        validateCategories(products);
        validateBrands(products);
        validateTags(products);
        validateProducts(products);
        return productRepository.saveAll(products);
    }

    private void validateTags(List<Product> products) throws NoRelatedEntityException {
        var tagIds = products.stream().flatMap(product -> product.getTags().stream().map(Tag::getId)).toList();
        var tags = tagService.getByIds(tagIds);

        if (tags.size() != new HashSet<>(tagIds).size()) {
            throw new NoRelatedEntityException("Some tags are not found");
        }

        // Map attached tags to product
        products.forEach(product -> {
            var productTags = new LinkedList<Tag>();
            product.getTags().forEach(tag -> {
                var foundTag = tags.stream().filter(t -> t.getId().equals(tag.getId())).findFirst();
                foundTag.ifPresent(productTags::add);
            });
            product.setTags(productTags);
        });
    }

    private void validateBrands(List<Product> products) throws NoRelatedEntityException {
        var brandIds = products.stream().flatMap(product -> product.getBrands().stream().map(Brand::getId)).toList();
        var brands = brandService.getByIds(brandIds);

        if (brands.size() != new HashSet<>(brandIds).size()) {
            throw new NoRelatedEntityException("Some brands are not found");
        }

        // Map attached brands to product
        products.forEach(product -> {
            var productBrands = new LinkedList<Brand>();
            product.getBrands().forEach(brand -> {
                var foundBrand = brands.stream().filter(b -> b.getId().equals(brand.getId())).findFirst();
                foundBrand.ifPresent(productBrands::add);
            });
            product.setBrands(productBrands);
        });
    }

    private void validateCategories(List<Product> products) throws NoRelatedEntityException {
        var categoryIds = products.stream().flatMap(product -> product.getCategories().stream().map(Category::getId)).toList();
        var categories = categoryService.getByIds(categoryIds);

        if (categories.size() != new HashSet<>(categoryIds).size()) {
            throw new NoRelatedEntityException("Some categories are not found");
        }

        // Map attached categories to product
        products.forEach(product -> {
            var productCategories = new LinkedList<Category>();
            product.getCategories().forEach(category -> {
                var foundCategory = categories.stream().filter(c -> c.getId().equals(category.getId())).findFirst();
                foundCategory.ifPresent(productCategories::add);
            });
            product.setCategories(productCategories);
        });
    }

    private void validateProducts(List<Product> products) throws NoRelatedEntityException {
        var skus = products.stream().map(Product::getSku).toList();
        var repeatedSku = productRepository.existsProductBySkuIn(skus);
        if(repeatedSku) throw new NoRelatedEntityException("Some products have repeated SKU at database");

        // Check if there are repeated SKU in the request (only if there are more than one product)
        if(!products.isEmpty() && (products.size() != new HashSet<>(skus).size()))
            throw new NoRelatedEntityException("Some products have repeated SKU in the request");
    }

    @Override
    public Product update(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> updateBatch(List<Product> products) {
        return productRepository.saveAll(products);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}

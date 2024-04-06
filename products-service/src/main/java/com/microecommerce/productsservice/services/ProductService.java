package com.microecommerce.productsservice.services;

import com.microecommerce.productsservice.exceptions.NoRelatedEntityException;
import com.microecommerce.productsservice.models.*;
import com.microecommerce.productsservice.repositories.ProductRepository;
import com.microecommerce.productsservice.services.interfaces.*;
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
    private final IDetailService detailService;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    public ProductService(ProductRepository productRepository, ICategoryService categoryService, IBrandService brandService, ITagService tagService, IDetailService detailService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.tagService = tagService;
        this.detailService = detailService;
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

    @Override
    public Product addTag(Long productId, Long tagId) throws NoRelatedEntityException {
        var product = productRepository.findById(productId).orElse(null);
        var tag = tagService.getById(tagId);

        if (product == null || tag == null) {
            throw new NoRelatedEntityException("Product or tag not found");
        }

        product.getTags().add(tag);
        return productRepository.save(product);

    }

    @Override
    public Product removeTag(Long productId, Long tagId) throws NoRelatedEntityException {
        var product = productRepository.findById(productId).orElse(null);
        var tag = tagService.getById(tagId);

        if (product == null || tag == null) {
            throw new NoRelatedEntityException("Product or tag not found");
        }

        product.getTags().remove(tag);
        return productRepository.save(product);
    }

    @Override
    public Product addCategory(Long productId, Long categoryId) throws NoRelatedEntityException {
        var product = productRepository.findById(productId).orElse(null);
        var category = categoryService.getById(categoryId);

        if (product == null || category == null) {
            throw new NoRelatedEntityException("Product or category not found");
        }

        product.getCategories().add(category);
        return productRepository.save(product);
    }

    @Override
    public Product removeCategory(Long productId, Long categoryId) throws NoRelatedEntityException {
        var product = productRepository.findById(productId).orElse(null);
        var category = categoryService.getById(categoryId);

        if (product == null || category == null) {
            throw new NoRelatedEntityException("Product or category not found");
        }

        product.getCategories().remove(category);
        return productRepository.save(product);
    }

    @Override
    public Product addDetails(Long productId, Map<Long, Object> details) throws NoRelatedEntityException {
        var product = productRepository.findById(productId).orElse(null);

        var detailsIds = details.keySet();
        var detailsEntities = detailService.getByIds(new ArrayList<>(detailsIds));

        if (product == null || detailsEntities.size() != detailsIds.size()) {
            throw new NoRelatedEntityException("Product or details not found");
        }

        detailsEntities.forEach(detail -> {
            var value = details.get(detail.getId());
            var productDetail = createDetailForProduct(product, detail, value);
            product.getProductDetails().add(productDetail);
        });

        return productRepository.save(product);
    }

    private ProductDetails createDetailForProduct(Product product, Detail detail, Object value) {
        var productDetail = new ProductDetails();
        productDetail.setProduct(product);
        productDetail.setDetail(detail);
        productDetail.setValue(value);
        return productDetail;
    }

    @Override
    public Product removeDetails(Long productId, List<Long> detailIds) throws NoRelatedEntityException {
        var product = productRepository.findById(productId).orElse(null);
        var details = detailService.getByIds(detailIds);

        if (product == null || details.size() != detailIds.size()) {
            throw new NoRelatedEntityException("Product or details not found");
        }

        product.getProductDetails().removeIf(productDetail -> details.stream().anyMatch(detail -> detail.getId().equals(productDetail.getDetail().getId())));
        return productRepository.save(product);
    }
}

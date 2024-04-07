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
import java.util.stream.Collectors;

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

        Set<Long> tagIds = new HashSet<>();
        Set<Long> brandIds = new HashSet<>();;
        Set<Long> categoryIds = new HashSet<>();;
        Set<Long> detailsIds = new HashSet<>();;

        // Get all related entities ids
        for(var product : products) {
            if(product.getTags() != null && !product.getTags().isEmpty())
                tagIds.addAll(product.getTags().stream().map(Tag::getId).toList());
            else product.setTags(new LinkedList<>());

            if(product.getBrands() != null && !product.getBrands().isEmpty())
                brandIds.addAll(product.getBrands().stream().map(Brand::getId).toList());
            else product.setBrands(new LinkedList<>());

            if(product.getCategories() != null && !product.getCategories().isEmpty())
                categoryIds.addAll(product.getCategories().stream().map(Category::getId).toList());
            else product.setCategories(new LinkedList<>());

            if(product.getProductDetails() != null && !product.getProductDetails().isEmpty())
                detailsIds.addAll(product.getProductDetails().stream().map(productDetail -> productDetail.getDetail().getId()).toList());
            else product.setProductDetails(new LinkedList<>());
        }

        // Get all related entities
        var tags = tagService.getByIds(new ArrayList<>(tagIds));
        var brands = brandService.getByIds(new ArrayList<>(brandIds));
        var categories = categoryService.getByIds(new ArrayList<>(categoryIds));
        var details = detailService.getByIds(new ArrayList<>(detailsIds));

        if(tags.size() != tagIds.size()) throw new NoRelatedEntityException("Some tags not found");
        if(brands.size() != brandIds.size()) throw new NoRelatedEntityException("Some brands not found");
        if(categories.size() != categoryIds.size()) throw new NoRelatedEntityException("Some categories not found");
        if(details.size() != detailsIds.size()) throw new NoRelatedEntityException("Some details not found");

        // Map attached tags, brands, categories and details to product (to avoid detached entity exception)
        for(Product product : products) {
            var productTags = new LinkedList<Tag>();
            for(Tag tag : product.getTags()) {
                var foundTag = tags.stream().filter(t -> t.getId().equals(tag.getId())).findFirst();
                foundTag.ifPresent(productTags::add);
            }
            product.setTags(productTags);

            var productBrands = new LinkedList<Brand>();
            for(Brand brand : product.getBrands()) {
                var foundBrand = brands.stream().filter(b -> b.getId().equals(brand.getId())).findFirst();
                foundBrand.ifPresent(productBrands::add);
            }
            product.setBrands(productBrands);

            var productCategories = new LinkedList<Category>();
            for(Category category : product.getCategories()) {
                var foundCategory = categories.stream().filter(c -> c.getId().equals(category.getId())).findFirst();
                foundCategory.ifPresent(productCategories::add);
            }
            product.setCategories(productCategories);

            var productDetails = new LinkedList<ProductDetails>();
            for(ProductDetails productDetail : product.getProductDetails()) {
                var foundDetail = details.stream().filter(d -> d.getId().equals(productDetail.getDetail().getId())).findFirst();
                foundDetail.ifPresent(detail -> {
                    productDetails.add(ProductDetails.createDetailForProduct(product, detail, productDetail.getValue()));
                });
            }
            product.setProductDetails(productDetails);
        }

        var skus = products.stream().map(Product::getSku).collect(Collectors.toList());
        var repeatedSku = productRepository.existsProductBySkuIn(skus);
        if(repeatedSku) throw new NoRelatedEntityException("Some products have repeated SKU at database");

        // Check if there are repeated SKU in the request (only if there are more than one product)
        if(!products.isEmpty() && (products.size() != new HashSet<>(skus).size()))
            throw new NoRelatedEntityException("Some products have repeated SKU in the request");

        return productRepository.saveAll(products);
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
            var productDetail = ProductDetails.createDetailForProduct(product, detail, value);
            product.getProductDetails().add(productDetail);
        });

        return productRepository.save(product);
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

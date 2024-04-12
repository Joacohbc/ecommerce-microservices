package com.microecommerce.productsservice.services;

import com.microecommerce.productsservice.dtos.ProductDTO;
import com.microecommerce.productsservice.exceptions.DuplicatedRelationException;
import com.microecommerce.productsservice.exceptions.EntityNotFoundException;
import com.microecommerce.productsservice.exceptions.NoRelatedEntityException;
import com.microecommerce.productsservice.models.*;
import com.microecommerce.productsservice.repositories.ProductDetailsRepository;
import com.microecommerce.productsservice.repositories.ProductRepository;
import com.microecommerce.productsservice.services.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public ProductService(ProductRepository productRepository, ICategoryService categoryService, IBrandService brandService, ITagService tagService, IDetailService detailService, ProductDetailsRepository productDetailsRepository) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.tagService = tagService;
        this.detailService = detailService;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getAllByPage(int page, int size, Product.ProductFields orderBy, Sort.Direction direction) {
        var sort = direction == Sort.Direction.ASC ? Sort.by(orderBy.getName()).ascending() : Sort.by(orderBy.getName()).descending();
        Pageable sortedByName = PageRequest.of(page, size, sort);
        return productRepository.findAll(sortedByName);
    }

    @Override
    public Product getById(Long id) throws EntityNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getByIds(List<Long> ids) {
        return productRepository.findAllById(ids);
    }

    @Override
    public Product create(Product product) throws NoRelatedEntityException, DuplicatedRelationException{
        return createBatch(List.of(product)).get(0);
    }

    @Override
    public List<Product> createBatch(List<Product> products) throws NoRelatedEntityException, DuplicatedRelationException {
        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> tagIds = new HashSet<>();
        Set<Long> brandIds = new HashSet<>();
        Set<Long> categoryIds = new HashSet<>();
        Set<Long> detailsIds = new HashSet<>();

        // Get All related Entities IDs
        for(var product : products) {
            if(product.getTags() != null && !product.getTags().isEmpty()) {
                tagIds.addAll(IGetId.getSetIds(product.getTags()));
            }
            else product.setTags(Collections.emptyList());

            if(product.getBrands() != null && !product.getBrands().isEmpty()) {
                brandIds.addAll(IGetId.getSetIds(product.getBrands()));
            }
            else product.setBrands(Collections.emptyList());

            if(product.getCategories() != null && !product.getCategories().isEmpty()) {
                categoryIds.addAll(IGetId.getSetIds(product.getCategories()));
            }
            else product.setCategories(Collections.emptyList());

            if(product.getProductDetails() != null && !product.getProductDetails().isEmpty()) {
                detailsIds.addAll(product.getProductDetails()
                        .stream()
                        .map(productDetail -> productDetail.getDetail().getId())
                        .toList());
            }
            else product.setProductDetails(Collections.emptyList());
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
            product.setId(null);

            var productTagsBd = new HashSet<Tag>();
            for(Tag tag : product.getTags()) {
                tags.stream()
                    .filter(t -> t.getId().equals(tag.getId()))
                    .findFirst()
                    .ifPresent(productTagsBd::add);
            }

            // If the product has more tags than the ones found in the database
            if(product.getTags().size() > productTagsBd.size())
                throw new NoRelatedEntityException("Repeated tags found in the request for the product: " + product.getSku());
            product.setTags(new ArrayList<>(productTagsBd));

            var productBrandsBd = new HashSet<Brand>();
            for(Brand brand : product.getBrands()) {
                brands.stream()
                        .filter(b -> b.getId().equals(brand.getId()))
                        .findFirst()
                        .ifPresent(productBrandsBd::add);
            }

            if(product.getBrands().size() > productBrandsBd.size())
                throw new DuplicatedRelationException("Repeated brands found in the request for the product: " + product.getSku());
            product.setBrands(new ArrayList<>(productBrandsBd));

            var productCategoriesBd = new HashSet<Category>();
            for(Category category : product.getCategories()) {
                categories.stream()
                        .filter(c -> c.getId().equals(category.getId()))
                        .findFirst()
                        .ifPresent(productCategoriesBd::add);
            }

            if(product.getCategories().size() > productCategoriesBd.size())
                throw new DuplicatedRelationException("Repeated categories found in the request for the product: " + product.getSku());
            product.setCategories(new ArrayList<>(productCategoriesBd));

            var productDetailsBd = new LinkedList<ProductDetails>();
            for(ProductDetails productDetail : product.getProductDetails()) {
                var detail = IGetId.getFirstMatch(details, productDetail.getDetail().getId());
                if(detail == null) throw new NoRelatedEntityException("Detail not found for the product: " + product.getSku());

                for (ProductDetails pd : productDetailsBd) {
                    if (pd.getDetail().getId().equals(detail.getId())) {
                        throw new DuplicatedRelationException("Repeated details found in the request for the product: " + product.getSku());
                    }
                }
                productDetailsBd.add(ProductDetails.createDetailForProduct(product, detail, productDetail.getValue()));
            }

            product.setProductDetails(new ArrayList<>(productDetailsBd));
        }

        var skus = products.stream().map(Product::getSku).collect(Collectors.toList());
        var repeatedSku = productRepository.existsProductBySkuIn(skus);
        if(repeatedSku) throw new NoRelatedEntityException("Some products have repeated SKU at database");

        // Check if there are repeated SKU in the request (only if there are more than one product)
        if(!products.isEmpty() && (products.size() != new HashSet<>(skus).size()))
            throw new NoRelatedEntityException("Some products have repeated SKU in the request");

        return productRepository.saveAll(products);
    }

    // TODO: Implement the update method
    @Override
    public Product update(Product product) {
        return productRepository.save(product);
    }

    // TODO: Implement the update method
    @Override
    public List<Product> updateBatch(List<Product> products) {
        return productRepository.saveAll(products);
    }

    @Override
    public void deleteById(Long id) throws EntityNotFoundException {
        var product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        product.setDeletedAt(LocalDateTime.now());
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    @Override
    public Product addTag(Long productId, Long tagId) throws EntityNotFoundException {
        var product = getById(productId);
        var tag = tagService.getById(tagId);
        product.getTags().add(tag);
        return productRepository.save(product);

    }

    @Override
    public Product removeTag(Long productId, Long tagId) throws EntityNotFoundException {
        var product = getById(productId);
        var tag = tagService.getById(tagId);

        product.getTags().remove(tag);
        return productRepository.save(product);
    }

    @Override
    public Product addCategory(Long productId, Long categoryId) throws EntityNotFoundException {
        var product = getById(productId);
        var category = categoryService.getById(categoryId);
        product.getCategories().add(category);
        return productRepository.save(product);
    }

    @Override
    public Product removeCategory(Long productId, Long categoryId) throws EntityNotFoundException {
        var product = getById(productId);
        var category = categoryService.getById(categoryId);
        product.getCategories().remove(category);
        return productRepository.save(product);
    }

    @Override
    public Product addDetails(Long productId, List<ProductDetails> details) throws NoRelatedEntityException, EntityNotFoundException {
        var product = getById(productId);

        var detailsIds = details.stream().map(productDetail -> productDetail.getDetail().getId()).collect(Collectors.toSet());

        var detailsEntitiesMap = detailService.getByIds(new ArrayList<>(detailsIds))
                .stream()
                .collect(Collectors.toMap(Detail::getId, detail -> detail));

        if (detailsEntitiesMap.size() != detailsIds.size()) {
            throw new NoRelatedEntityException("Some details are not found");
        }

        for (ProductDetails productDetail : details) {
            var pd = ProductDetails.createDetailForProduct(
                    product,
                    detailsEntitiesMap.get(productDetail.getDetail().getId()),
                    productDetail.getValue());

            product.getProductDetails().add(pd);
        }

        return productRepository.save(product);
    }

    @Override
    public Product removeDetails(Long productId, List<Long> detailIds) throws EntityNotFoundException, NoRelatedEntityException {
        var product = getById(productId);
        var details = detailService.getByIds(detailIds);

        if (details.size() != detailIds.size()) {
            throw new NoRelatedEntityException("Some details are not found");
        }

        product.getProductDetails().removeIf(productDetail -> detailIds.contains(productDetail.getDetail().getId()));
        return productRepository.save(product);
    }
}

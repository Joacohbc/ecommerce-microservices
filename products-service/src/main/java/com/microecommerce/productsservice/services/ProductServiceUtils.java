package com.microecommerce.productsservice.services;

import com.microecommerce.productsservice.models.*;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ProductServiceUtils {
    private final TagService tagService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final DetailService detailService;

    public ProductServiceUtils(TagService tagService, BrandService brandService, CategoryService categoryService, DetailService detailService) {
        this.tagService = tagService;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.detailService = detailService;
    }

    public record ProductRelatedInfo(List<Tag> tags, List<Brand> brands, List<Category> categories, List<Detail> details) {}
    public record ProductRelatedInfoIds(List<Long> tagIds, List<Long> brandIds, List<Long> categoryIds, List<Long> detailsIds) {}

    public ProductRelatedInfoIds getProductRelatedInfoIds(List<Product> products) {
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

        return new ProductRelatedInfoIds(new ArrayList<>(tagIds), new ArrayList<>(brandIds), new ArrayList<>(categoryIds), new ArrayList<>(detailsIds));
    }

    public ProductRelatedInfo getProductRelatedInfo(ProductRelatedInfoIds productRelatedInfoIds) {
        List<Tag> tags = tagService.getByIds(productRelatedInfoIds.tagIds);
        List<Brand> brands = brandService.getByIds(productRelatedInfoIds.brandIds);
        List<Category> categories = categoryService.getByIds(productRelatedInfoIds.categoryIds);
        List<Detail> details = detailService.getByIds(productRelatedInfoIds.detailsIds);

        return new ProductRelatedInfo(tags, brands, categories, details);
    }
}

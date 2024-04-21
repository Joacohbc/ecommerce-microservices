package com.microecommerce.productsservice.services;

import com.microecommerce.productsservice.models.*;
import com.microecommerce.productsservice.repositories.ProductDetailsRepository;
import com.microecommerce.productsservice.repositories.ProductRepository;
import com.microecommerce.productsservice.services.interfaces.*;
import com.microecommerce.utilitymodule.exceptions.DuplicatedRelationException;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;
import com.microecommerce.utilitymodule.exceptions.RelatedEntityNotFoundException;
import jakarta.validation.Valid;
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
    private final ProductServiceUtils productServiceUtils;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductDetailsRepository productDetailsRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ICategoryService categoryService, IBrandService brandService, ITagService tagService, IDetailService detailService, ProductServiceUtils productServiceUtils, ProductDetailsRepository productDetailsRepository) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.tagService = tagService;
        this.detailService = detailService;
        this.productServiceUtils = productServiceUtils;
        this.productDetailsRepository = productDetailsRepository;
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
    public boolean checkProductsExistence(List<Long> productIds) {
        return productRepository.existsProductsByIdIn(productIds);
    }

    @Override
    public Product create(@Valid Product product) throws RelatedEntityNotFoundException, DuplicatedRelationException, InvalidEntityException {
        return createBatch(Collections.singletonList(product)).get(0);
    }

    @Override
    public List<Product> createBatch(@Valid List<Product> products) throws RelatedEntityNotFoundException, DuplicatedRelationException, InvalidEntityException {
        if (products.isEmpty()) return Collections.emptyList();

        // Get all related entities Ids and Information for all products
        var productRelatedInfoIds = productServiceUtils.getProductRelatedInfoIds(products);
        var productRelatedInfo = productServiceUtils.getProductRelatedInfo(productRelatedInfoIds);

        validateRelatedEntitiesExist(productRelatedInfo, productRelatedInfoIds);
        validateDuplicateRelations(products, true, productRelatedInfo);
        validateDuplicatedSkus(products);

        return productRepository.saveAll(products);
    }

    @Override
    public Product update(@Valid Product product) throws InvalidEntityException, EntityNotFoundException, RelatedEntityNotFoundException, DuplicatedRelationException {
        return updateBatch(List.of(product)).get(0);
    }

    @Override
    public List<Product> updateBatch(@Valid List<Product> products) throws InvalidEntityException, EntityNotFoundException, RelatedEntityNotFoundException, DuplicatedRelationException  {
        if (products.isEmpty()) throw new EntityNotFoundException("No products found");
        if(!IGetId.allHaveId(products)) throw new InvalidEntityException("All products must have an ID to be updated");

        var productIds = IGetId.getUniqueIdList(products);
        var productsBd = getByIds(productIds);

        if (productsBd.size() < products.size()) throw new EntityNotFoundException("Some products are not found");
        if(products.size() > IGetId.getIds(products).size()) throw new RelatedEntityNotFoundException("Some products are repeated");

        // Get all related entities Ids and Information for all products
        var productRelatedInfoIds = productServiceUtils.getProductRelatedInfoIds(products);
        var productRelatedInfo = productServiceUtils.getProductRelatedInfo(productRelatedInfoIds);

        // Validate if all related entities are found
        validateRelatedEntitiesExist(productRelatedInfo, productRelatedInfoIds);
        validateDuplicateRelations(products, false, productRelatedInfo);

        products.forEach(product -> {
            var productBd = IGetId.getFirstMatch(productsBd, product.getId());
            if(productBd == null) return;

            // Overwrite the fields that can't be updated by the user
            product.setIsDeleted(productBd.getIsDeleted());
            product.setDeletedAt(productBd.getDeletedAt());
            product.setSku(productBd.getSku());
        });

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
    public Product addTag(Long productId, Long tagId) throws DuplicatedRelationException, EntityNotFoundException {
        var product = getById(productId);
        var tag = tagService.getById(tagId);

        if(product.getTags().contains(tag)) throw new DuplicatedRelationException("Tag already added to the product");
        product.getTags().add(tag);
        return productRepository.save(product);
    }

    @Override
    public Product removeTag(Long productId, Long tagId) throws EntityNotFoundException, RelatedEntityNotFoundException {
        var product = getById(productId);
        var tag = tagService.getById(tagId);

        if(!product.getTags().contains(tag)) throw new RelatedEntityNotFoundException("Tag not found in the product");
        product.getTags().remove(tag);
        return productRepository.save(product);
    }

    @Override
    public Product addCategory(Long productId, Long categoryId) throws EntityNotFoundException, DuplicatedRelationException {
        var product = getById(productId);
        var category = categoryService.getById(categoryId);

        if(product.getCategories().contains(category)) throw new DuplicatedRelationException("Category already added to the product");
        product.getCategories().add(category);
        return productRepository.save(product);
    }

    @Override
    public Product removeCategory(Long productId, Long categoryId) throws EntityNotFoundException, RelatedEntityNotFoundException {
        var product = getById(productId);
        var category = categoryService.getById(categoryId);

        if(!product.getCategories().contains(category)) throw new RelatedEntityNotFoundException("Category not found in the product");
        product.getCategories().remove(category);
        return productRepository.save(product);
    }

    @Override
    public Product addDetails(Long productId, List<ProductDetails> details) throws RelatedEntityNotFoundException, EntityNotFoundException, DuplicatedRelationException {
        var product = getById(productId);
        var detailsIds = details.stream().map(productDetail -> productDetail.getDetail().getId()).collect(Collectors.toSet());

        var detailsEntitiesMap = detailService.getByIds(new ArrayList<>(detailsIds))
                .stream()
                .collect(Collectors.toMap(Detail::getId, detail -> detail));

        if (detailsEntitiesMap.size() != detailsIds.size()) {
            throw new RelatedEntityNotFoundException("Some details are not found in the database");
        }

        for (ProductDetails productDetail : details) {
            var pd = ProductDetails.createDetailForProduct(
                    product,
                    detailsEntitiesMap.get(productDetail.getDetail().getId()),
                    productDetail.getValue());

            boolean repeated = product.getProductDetails().stream()
                    .anyMatch(productDetails ->
                            productDetails.getDetail().getId()
                            .equals(
                                pd.getDetail().getId()
                            ));
            if(repeated) throw new DuplicatedRelationException("Detail already added to the product");

            product.getProductDetails().add(pd);
        }

        return productRepository.save(product);
    }

    @Override
    public Product removeDetails(Long productId, List<Long> detailIds) throws EntityNotFoundException, RelatedEntityNotFoundException {
        var product = getById(productId);
        var details = detailService.getByIds(detailIds);

        if (details.size() != detailIds.size()) {
            throw new RelatedEntityNotFoundException("Some details are not found in the database");
        }

        List<ProductDetails> productDetailsToDel = new ArrayList<>();
        for(Long detailId : detailIds) {
            var productDetail = product.getProductDetails().stream()
                    .filter(pd -> pd.getDetail().getId().equals(detailId))
                    .findFirst()
                    .orElseThrow(() -> new RelatedEntityNotFoundException("Detail not found in the product"));
            product.getProductDetails().remove(productDetail);
            productDetailsToDel.add(productDetail);
        }
        productDetailsRepository.deleteAll(productDetailsToDel);
        return product;
    }

    @Override
    public Product updateDetail(Long productId, Long detailId, ProductDetails value) throws EntityNotFoundException, RelatedEntityNotFoundException {
        var product = getById(productId);
        var detail = detailService.getById(detailId);

        var productDetailsToUpd = product.getProductDetails().stream()
                .filter(productDetail -> productDetail.getDetail().getId().equals(detail.getId()))
                .findFirst()
                .orElseThrow(() -> new RelatedEntityNotFoundException("Detail not found in the product"));

        productDetailsToUpd.setValue(value.getValue());
        productDetailsToUpd.setAdditionalInfo(value.getAdditionalInfo());
        return productRepository.save(product);
    }

    private void validateDuplicatedSkus(List<Product> products) throws RelatedEntityNotFoundException, InvalidEntityException {
        var skus = products.stream().map(Product::getSku).collect(Collectors.toList());
        var repeatedSku = productRepository.existsProductBySkuIn(skus);
        if(repeatedSku) throw new RelatedEntityNotFoundException("Some products have repeated SKU at database");

        // Check if there are repeated SKU in the request (only if there are more than one product)
        if(!products.isEmpty() && (products.size() != new HashSet<>(skus).size()))
            throw new InvalidEntityException("Some products have repeated SKU in the request");
    }


    public void validateRelatedEntitiesExist(ProductServiceUtils.ProductRelatedInfo productRelatedInfo,
                                             ProductServiceUtils.ProductRelatedInfoIds productRelatedInfoIds) throws RelatedEntityNotFoundException {

        var tags = productRelatedInfo.tags();
        var brands = productRelatedInfo.brands();
        var categories = productRelatedInfo.categories();
        var details = productRelatedInfo.details();

        var tagsIds = productRelatedInfoIds.tagIds();
        var brandsIds = productRelatedInfoIds.brandIds();
        var categoriesIds = productRelatedInfoIds.categoryIds();
        var detailsIds = productRelatedInfoIds.detailsIds();

        if(tags.size() != tagsIds.size()) throw new RelatedEntityNotFoundException("Some tags are not found");
        if(brands.size() != brandsIds.size()) throw new RelatedEntityNotFoundException("Some brands are not found");
        if(categories.size() != categoriesIds.size()) throw new RelatedEntityNotFoundException("Some categories are not found");
        if(details.size() != detailsIds.size()) throw new RelatedEntityNotFoundException("Some details are not found");
    }

    public void validateDuplicateRelations(List<Product> products, boolean removeIds,
                                           ProductServiceUtils.ProductRelatedInfo productRelatedInfo) throws RelatedEntityNotFoundException, DuplicatedRelationException {

        var tags = productRelatedInfo.tags();
        var brands = productRelatedInfo.brands();
        var categories = productRelatedInfo.categories();
        var details = productRelatedInfo.details();

        // Extract the tags, brands and categories from the database and overwrite the ones in the request
        // to avoid duplicated objects in the database. HashSet because the Tags, Brands and Categories are
        // the same object in the database so reference to the same object at Java when are retrieved from the database
        // For ProductDetails, the object is different because the value is different at database so in Java is a
        // different object
        for(Product product : products) {
            if(removeIds) product.setId(null);

            var productTagsBd = new HashSet<Tag>();
            for(Tag tag : product.getTags()) {
                tags.stream()
                        .filter(t -> t.getId().equals(tag.getId()))
                        .findFirst()
                        .ifPresent(productTagsBd::add);
            }

            // If the product has more tags than the ones found in the database
            if(product.getTags().size() > productTagsBd.size())
                throw new DuplicatedRelationException("Repeated tags found in the request for the product: " + product.getSku());
            product.setTags(new ArrayList<>(productTagsBd));

            var productBrandsBd = new HashSet<Brand>();
            for(Brand brand : product.getBrands()) {
                brands.stream()
                        .filter(b -> b.getId().equals(brand.getId()))
                        .findFirst()
                        .ifPresent(productBrandsBd::add);
            }

            // If the product has more brands than the ones found in the database
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

            // If the product has more categories than the ones found in the database
            if(product.getCategories().size() > productCategoriesBd.size())
                throw new DuplicatedRelationException("Repeated categories found in the request for the product: " + product.getSku());
            product.setCategories(new ArrayList<>(productCategoriesBd));

            var productDetailsBd = new LinkedList<ProductDetails>();
            for(ProductDetails productDetail : product.getProductDetails()) {
                var detail = IGetId.getFirstMatch(details, productDetail.getDetail().getId());
                if(detail == null) throw new RelatedEntityNotFoundException("Some details are not found");

                // Check if the detail is already in the list
                for (ProductDetails pd : productDetailsBd) {
                    if (pd.getDetail().getId().equals(detail.getId())) {
                        throw new DuplicatedRelationException("Repeated details found in the request for the product: " + product.getSku());
                    }
                }
                productDetailsBd.add(ProductDetails.createDetailForProduct(product, detail, productDetail.getValue()));
            }
            product.setProductDetails(productDetailsBd);
        }

    }
}

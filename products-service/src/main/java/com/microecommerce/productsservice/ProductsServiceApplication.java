package com.microecommerce.productsservice;

import com.microecommerce.productsservice.models.Brand;
import com.microecommerce.productsservice.models.Category;
import com.microecommerce.productsservice.models.Product;
import com.microecommerce.productsservice.models.Tag;
import com.microecommerce.productsservice.repositories.BrandRepository;
import com.microecommerce.productsservice.repositories.CategoryRepository;
import com.microecommerce.productsservice.repositories.ProductRepository;
import com.microecommerce.productsservice.repositories.TagRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;

@SpringBootApplication
public class ProductsServiceApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(ProductsServiceApplication.class, args);
    }

    public static void createTestData(ApplicationContext context) {
        ProductRepository productRepository = context.getBean(ProductRepository.class);
        BrandRepository brandRepository = context.getBean(BrandRepository.class);
        TagRepository tagRepository = context.getBean(TagRepository.class);
        CategoryRepository categoryRepository = context.getBean(CategoryRepository.class);

        Category category = new Category();
        category.setName("Electronics");
        category.setDescription("Electronics");
        categoryRepository.save(category);

        Category category2 = new Category();
        category2.setName("Clothing");
        category2.setDescription("Clothing");
        categoryRepository.save(category2);

        Category category3 = new Category();
        category3.setName("Books");
        category3.setDescription("Books");
        categoryRepository.save(category3);

        Brand brand = new Brand();
        brand.setName("Apple");
        brandRepository.save(brand);

        Brand brand2 = new Brand();
        brand2.setName("Samsung");
        brandRepository.save(brand2);

        Brand brand3 = new Brand();
        brand3.setName("Nike");
        brandRepository.save(brand3);

        Tag tag = new Tag();
        tag.setName("Smartphone");
        tag.setDescription("Smartphone");
        tagRepository.save(tag);

        Tag tag2 = new Tag();
        tag2.setName("Tablet");
        tag2.setDescription("Tablet");
        tagRepository.save(tag2);

        Tag tag3 = new Tag();
        tag3.setName("Laptop");
        tag3.setDescription("Laptop");
        tagRepository.save(tag3);

        Tag tag4 = new Tag();
        tag4.setName("Shoes");
        tag4.setDescription("Shoes");
        tagRepository.save(tag4);

        Product product = new Product();
        product.setName("iPhone 12");
        product.setOriginalPrice(1000.0);
        product.getCategories().add(category);
        product.getBrands().add(brand);
        product.getTags().add(tag);
        productRepository.save(product);

        Product product1 = new Product();
        product1.setName("Shoes XXL");
        product1.setOriginalPrice(100.0);
        product1.getCategories().add(category2);
        product1.getTags().add(tag4);
        product1.getBrands().add(brand3);
        productRepository.save(product1);
    }
}

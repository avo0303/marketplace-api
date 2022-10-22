package com.andrewsha.marketplace.domain.product;

import java.io.File;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andrewsha.marketplace.domain.product.image.ProductImageRepository;
import com.andrewsha.marketplace.domain.product.request.UpdateProductForm;
import com.andrewsha.marketplace.exception.ProductServiceException;

@Service
public class ProductService {
    @Autowired
    private Logger logger;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;

    @Value("${upload.path.products}")
    String uploadPath;

    public Page<Product> getProducts(int page, int size) {
        // TODO check get
        return this.productRepository.findAll(PageRequest.of(page, size));
    }

    public Product getProduct(UUID id) {
        return this.productRepository.findById(id).orElseThrow(
                () -> new ProductServiceException("product with id " + id + " does not exists"));
    }

    @Transactional
    public Product createProduct(Product product) {
        this.productRepository.save(product);
        product.setImages(this.productImageRepository.saveAll(product.getImages()));
        this.logger.info("create product {} to the database", product.getId());
        return product;
    }

    @Transactional
    public Product patchProduct(UUID id, UpdateProductForm productDetails) {
        Product product = this.productRepository.findById(id).orElseThrow(
                () -> new ProductServiceException("product with id " + id + " does not exists"));
        if (productDetails.getName() != null) {
            product.setName(productDetails.getName());
        }
        if (productDetails.getActualPrice() != null) {
            product.setActualPrice(productDetails.getActualPrice());
        }
        if (productDetails.getDiscount() != null) {
            product.setDiscount(productDetails.getDiscount());
        }
        if (productDetails.getImages() != null) {
            if (!productDetails.getImages().isEmpty()) {
                product.setImages(productDetails.getImages());
            }
        }
        if (productDetails.getAttributes() != null) {
            if (!productDetails.getAttributes().isEmpty()) {
                product.setAttributes(productDetails.getAttributes());
            }
        }
        return this.productRepository.save(product);
    }

    @Transactional
    public Product putProduct(UUID id, UpdateProductForm productDetails) {
        Product product = this.productRepository.findById(id).orElseThrow(
                () -> new ProductServiceException("product with id " + id + " does not exists"));

        product.setName(productDetails.getName());
        product.setActualPrice(productDetails.getActualPrice());
        product.setDiscount(productDetails.getDiscount());
        product.setImages(productDetails.getImages());
        product.setAttributes(productDetails.getAttributes());
        return this.productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        Product product = this.productRepository.findById(id).orElseThrow(
                () -> new ProductServiceException("product with id " + id + " does not exists"));
        product.deleteProductCards();
        File storageFolder = new File(uploadPath + "/" + id);
        if (storageFolder.exists()) {
            storageFolder.delete();
        }
        this.productRepository.deleteById(id);
    }
}

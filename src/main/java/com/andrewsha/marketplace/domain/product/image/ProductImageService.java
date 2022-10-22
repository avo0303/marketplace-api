package com.andrewsha.marketplace.domain.product.image;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.andrewsha.marketplace.domain.product.Product;
import com.andrewsha.marketplace.domain.product.ProductRepository;
import com.andrewsha.marketplace.exception.ProductServiceException;
import com.andrewsha.marketplace.storage.StorageService;

@Service
public class ProductImageService {
    @Autowired
    private StorageService storageService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;

    @Value("${server.address}")
    String serverAddress;
    @Value("${server.port}")
    String serverPort;
    @Value("${upload.path.products}")
    String uploadPath;

    @Transactional
    public Product putImageAsFile(UUID id, MultipartFile[] files)
            throws IllegalStateException, IOException {
        Product product = this.productRepository.findById(id).orElseThrow(
                () -> new ProductServiceException("product with id " + id + " does not exists"));
        for (int i = 0; i < files.length; i++) {
            String fileName = this.storageService.store(files[i],
                    uploadPath + "/" + product.getId().toString());
            if (fileName != null) {
                String url = this.serverAddress + ":" + this.serverPort + "/storage/products/"
                        + product.getId().toString() + "/" + fileName;
                ProductImage productImage =
                        this.productImageRepository.save(new ProductImage(url, product));
                product.addImage(productImage);
            }
        }
        return product;
    }

    public boolean deleteImageFromStorage(ProductImage productImage) {
        String imageName = productImage.getUrl().substring(productImage.getUrl().lastIndexOf("/"));
        File image = new File(uploadPath + "/" + productImage.getProduct().getId() + imageName);
        if (image.exists()) {
            return image.delete();
        }
        return false;
    }

    @Transactional
    public boolean deleteProductImage(UUID productId, UUID imageId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException(
                        "product with id " + productId + " does not exists"));

        for (ProductImage productImage : product.getImages()) {
            if (productImage.getId().equals(imageId)) {
                this.productImageRepository.deleteById(imageId);
                return this.deleteImageFromStorage(productImage);
            }
        }
        throw new ProductServiceException("product with id " + productId + " : image with id "
                + imageId + " does not exists");
    }

    @Transactional
    public Product putImageAsUrl(UUID productId, ProductImageList productImageList) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException(
                        "product with id " + productId + " does not exists"));
        for (ProductImage productImage : productImageList.getProductImages()) {
            productImage.setProduct(product);
            this.productImageRepository.save(productImage);
            product.addImage(productImage);
        }
        return product;
    }
}

package com.andrewsha.marketplace.domain.product.image;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import com.andrewsha.marketplace.domain.product.Product;
import com.andrewsha.marketplace.domain.product.ProductRepository;
import com.andrewsha.marketplace.exception.ProductServiceException;
import com.andrewsha.marketplace.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class ProductImageService {
	@Autowired
	private StorageService storageService;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductImageRepository productImageRepository;

	@Value("${api.endpoint.storage.products}")
	String endpoint;

	@Value("${upload.path.products}")
	String uploadPath;

	@Transactional
	public Product upload(Product product, MultiValueMap<String, MultipartFile> files) {
		String baseUrl = ServletUriComponentsBuilder.fromCurrentRequest().replacePath(endpoint)
				.toUriString();
		for (Map.Entry<String, List<MultipartFile>> entry : files.entrySet()) {
			Collection<String> paths = this.storageService.storeAll(entry.getValue(),
					uploadPath + "/" + product.getId().toString());
			List<ProductImage> images = paths.stream().map(e -> {
				String url = baseUrl + product.getId().toString() + "/" + e;
				return new ProductImage(url, product);
			}).collect(Collectors.toList());
			this.productImageRepository.saveAll(images);
			product.addImages(images);
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
			product.addImages(List.of(productImage));
		}
		return product;
	}

	public List<ProductImage> saveAll(Iterable<ProductImage> images) {
		return this.productImageRepository.saveAll(images);
	}
}

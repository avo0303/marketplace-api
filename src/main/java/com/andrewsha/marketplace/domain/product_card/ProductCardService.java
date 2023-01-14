package com.andrewsha.marketplace.domain.product_card;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.MultipartFile;
import com.andrewsha.marketplace.domain.product.Product;
import com.andrewsha.marketplace.domain.product.ProductRepository;
import com.andrewsha.marketplace.domain.product.request.CreateProductForm;
import com.andrewsha.marketplace.domain.product_card.request.CreateProductCardForm;
import com.andrewsha.marketplace.domain.product_card.request.UpdateProductCardForm;
import com.andrewsha.marketplace.domain.store.Store;
import com.andrewsha.marketplace.domain.store.StoreService;
import com.andrewsha.marketplace.exception.ProductCardServiceException;
import com.andrewsha.marketplace.exception.ProductServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductCardService {
	@Autowired
	private ProductCardRepository productCardRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private StoreService storeService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public Page<ProductCard> getProductCards(MultiValueMap<String, String> params)
			throws MissingServletRequestParameterException {
		// TODO sorting by rating
		int page, size;
		String category;
		if (!params.containsKey("page[number]")) {
			throw new MissingServletRequestParameterException("page[number]", "int");
		}
		if (!params.containsKey("page[size]")) {
			throw new MissingServletRequestParameterException("page[size]", "int");
		}
		try {
			page = Integer.parseInt(params.remove("page[number]").get(0));
			size = Integer.parseInt(params.remove("page[size]").get(0));
		} catch (NumberFormatException e) {
			throw new ProductCardServiceException("given page (size) parameter is not valid");
		}
		if (params.containsKey("category")) {
			category = params.remove("category").get(0);
			if (!params.isEmpty()) {
				String json;
				try {
					json = new ObjectMapper().writeValueAsString(params.toSingleValueMap());
					return this.productCardRepository.findByCategoryAndAttributes(category, json,
							PageRequest.of(page, size));
				} catch (JsonProcessingException e) {
					throw new ProductCardServiceException("failed to parse attributes");
				}
			}
			return this.productCardRepository.findByCategory(category, PageRequest.of(page, size));
		}
		if (!params.isEmpty()) {
			String json;
			try {
				json = new ObjectMapper().writeValueAsString(params.toSingleValueMap());
				return this.productCardRepository.findByAttributes(json,
						PageRequest.of(page, size));
			} catch (JsonProcessingException e) {
				throw new ProductCardServiceException("failed to parse attributes");
			}
		}
		return this.productCardRepository.findAll(PageRequest.of(page, size));
	}

	public ProductCard getProductCard(UUID id) {
		return this.productCardRepository.findById(id)
				.orElseThrow(() -> new ProductServiceException(
						"product card with id " + id + " does not exists"));
	}

	// TODO
	@Transactional
	public ProductCard createProductCard(CreateProductCardForm productCardDetails,
			MultiValueMap<String, MultipartFile> files) {
		for (Entry<String, List<MultipartFile>> entry : files.entrySet()) {
			//TODO save files, set images for products
		}
		Store store = this.storeService.getStore(productCardDetails.getStore());
		ProductCard productCard = new ProductCard();
		productCard.setStore(store);
		productCard.setName(productCardDetails.getName());
		productCard.setShortDescription(productCardDetails.getShortDescription());
		productCard.setDescription(productCardDetails.getDescription());
		/*
		 * for (CreateProductForm product : productCardDetails.getProducts() {
		 * productCard.addProduct(this.productRepository.save(prod)); }
		 */
		productCard.setCategory(productCardDetails.getCategory());
		return this.productCardRepository.save(productCard);
	}

	@Transactional
	public ProductCard patchProductCard(UUID id, UpdateProductCardForm productDetails) {
		ProductCard productCard = this.productCardRepository.findById(id)
				.orElseThrow(() -> new ProductServiceException(
						"product card with id " + id + " does not exists"));
		if (productDetails.getName() != null) {
			productCard.setName(productDetails.getName());
		}
		if (productDetails.getShortDescription() != null) {
			productCard.setShortDescription(productDetails.getShortDescription());
		}
		if (productDetails.getDescription() != null) {
			productCard.setDescription(productDetails.getDescription());
		}
		if (productDetails.getProducts() != null) {
			productCard.setProducts(productDetails.getProducts());
		}
		if (productDetails.getCategory() != null) {
			productCard.setCategory(productDetails.getCategory());
		}
		return this.productCardRepository.save(productCard);
	}

	@Transactional
	public ProductCard putProductCard(UUID id, UpdateProductCardForm productDetails) {
		ProductCard productCard = this.productCardRepository.findById(id)
				.orElseThrow(() -> new ProductCardServiceException(
						"product card with id " + id + " does not exists"));
		productCard.setName(productDetails.getName());
		productCard.setShortDescription(productDetails.getShortDescription());
		productCard.setDescription(productDetails.getDescription());
		productCard.setProducts(productDetails.getProducts());
		productCard.setCategory(productDetails.getCategory());

		return productCard;
	}

	@Transactional
	public ProductCard putProductToProductCard(UUID productCardId, Product product) {
		ProductCard productCard = this.productCardRepository.findById(productCardId)
				.orElseThrow(() -> new ProductCardServiceException(
						"product card with id " + productCardId + " does not exists"));
		productCard.addProduct(product);
		return this.productCardRepository.save(productCard);
	}

	public void deleteProductCard(UUID id) {
		this.productCardRepository.deleteById(id);
	}
}

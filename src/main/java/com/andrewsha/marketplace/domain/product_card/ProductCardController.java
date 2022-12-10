package com.andrewsha.marketplace.domain.product_card;

import static com.toedter.spring.hateoas.jsonapi.MediaTypes.JSON_API_VALUE;
import java.util.UUID;
import javax.validation.Valid;
import com.andrewsha.marketplace.domain.product.Product;
import com.andrewsha.marketplace.domain.product_card.request.CreateProductCardForm;
import com.andrewsha.marketplace.domain.product_card.request.UpdateProductCardForm;
import com.andrewsha.marketplace.domain.product_card.resource.ModelBuilder;
import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/product-card")
@Validated
public class ProductCardController {
	@Autowired
	private ProductCardService productCardService;
	// TODO change to interface
	@Autowired
	private ModelBuilder builder;

	@GetMapping(produces = JSON_API_VALUE)
	public ResponseEntity<?> getProductCards(
			// @RequestParam @Min(0) int page, @RequestParam @Min(1) int size,
			// @RequestParam(required = false) Optional<String> category,
			@RequestParam(required = true) MultiValueMap<String, String> params)
			throws MissingServletRequestParameterException {
		return ResponseEntity
				.ok(this.builder.build(this.productCardService.getProductCards(params)));
	}

	@GetMapping(path = "/{productCardId}", produces = JSON_API_VALUE)
	public ResponseEntity<?> getProductCard(@PathVariable("productCardId") UUID id) {
		// TODO допилить спеку
		return ResponseEntity.ok(this.builder.build(this.productCardService.getProductCard(id)));
	}

	@PostMapping()
	@PreAuthorize("hasPermission(#productCard.store, 'ProductCard', 'CREATE')")
	public ResponseEntity<?> createProductCard(
			@Valid @RequestBody CreateProductCardForm productCard) {
		return ResponseEntity
				.ok(this.builder.build(this.productCardService.createProductCard(productCard)));
	}

	@PatchMapping(path = "{productCardId}")
	@PreAuthorize("hasPermission(#id, 'ProductCard', 'UPDATE')")
	public ResponseEntity<?> patchProductCard(@PathVariable("productCardId") UUID id,
			@Valid @RequestBody UpdateProductCardForm productCardDetails) {
		return ResponseEntity.ok(this.builder
				.build(this.productCardService.patchProductCard(id, productCardDetails)));
	}

	@PutMapping(path = "{productCardId}")
	@PreAuthorize("hasPermission(#id, 'ProductCard', 'UPDATE')")
	public ResponseEntity<?> putProductCard(@PathVariable("productCardId") UUID id,
			@Valid @RequestBody UpdateProductCardForm productCardDetails) {
		return ResponseEntity.ok(
				this.builder.build(this.productCardService.putProductCard(id, productCardDetails)));
	}

	@PutMapping(path = "{productCardId}/products/")
	@PreAuthorize("hasPermission(#productCardId, 'ProductCard', 'UPDATE')")
	public ResponseEntity<?> putProductToProductCard(
			@PathVariable("productCardId") UUID productCardId,
			@Valid @RequestBody Product productDetails) {
		return ResponseEntity.ok(this.builder.build(
				this.productCardService.putProductToProductCard(productCardId, productDetails)));
	}

	@DeleteMapping(path = "{productCardId}")
	@PreAuthorize("hasPermission(#id, 'ProductCard', 'DELETE')")
	public ResponseEntity<?> deleteProductCard(@PathVariable("productCardId") UUID id) {
		this.productCardService.deleteProductCard(id);
		return ResponseEntity.ok(JsonApiModelBuilder.jsonApiModel().meta("deleted", id));
	}
}

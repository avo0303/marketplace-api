package com.andrewsha.marketplace.domain.product;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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

import com.andrewsha.marketplace.domain.product.request.UpdateProductForm;

@RestController
@RequestMapping(path = "api/v1/product")
@Validated
public class ProductController {
	private final ProductService productService;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<?> getProducts(
			@RequestParam(value = "page[number]", required = true) @Min(0) int page,
			@RequestParam(value = "page[size]", required = true) @Min(1) int size) {
		Page<Product> productsPage = this.productService.getProducts(page, size);
		return ResponseEntity.ok(productsPage);
	}

	@GetMapping(path = "/{productId}")
	public ResponseEntity<?> getProduct(@PathVariable("productId") UUID id) {
		return ResponseEntity.ok(this.productService.getProduct(id));
	}

	@PostMapping
	@PreAuthorize("hasPermission(#product, 'CREATE')")
	public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) {
		return ResponseEntity.ok(this.productService.createProduct(product));
	}

	@PatchMapping(path = "{productId}")
	@PreAuthorize("hasPermission(#id, 'Product', 'UPDATE')")
	public ResponseEntity<?> updateProduct(@PathVariable("productId") UUID id,
			@Valid @RequestBody UpdateProductForm productDetails) {
		return ResponseEntity.ok(this.productService.patchProduct(id, productDetails));
	}

	@PutMapping(path = "{productId}")
	@PreAuthorize("hasPermission(#id, 'Product', 'UPDATE')")
	public ResponseEntity<?> putProduct(@PathVariable("productId") UUID id,
			@Valid @RequestBody UpdateProductForm productDetails) {
		return ResponseEntity.ok(this.productService.putProduct(id, productDetails));
	}

	@DeleteMapping(path = "{productId}")
	@PreAuthorize("hasPermission(#id, 'Product', 'DELETE')")
	public ResponseEntity<?> deleteProduct(@PathVariable("productId") UUID id) {
		this.productService.deleteProduct(id);
		return ResponseEntity.ok("product with id " + id + " successfully deleted");
	}
}

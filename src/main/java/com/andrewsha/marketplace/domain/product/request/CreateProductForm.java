package com.andrewsha.marketplace.domain.product.request;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateProductForm {
	@Size(min = 3, max = 20,
			message = "product name must be greater than 3 but less than 20 characters")
    @NotBlank(message = "name cannot be empty")
	private String name;

	@Min(value = 1, message = "price must be greater than 1")
    @NotNull(message = "actual price cannot be null")
	private Double actualPrice;

	@Min(value = 0, message = "discount must be equal or greater than 0")
	@Max(value = 99, message = "discount must be equal or less than 99")
	private Double discount;

	// valid url or uploaded file key name
	@NotEmpty(message = "set of images cannot be empty")
	private List<String> images;

	private Map<String, String> attributes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
}

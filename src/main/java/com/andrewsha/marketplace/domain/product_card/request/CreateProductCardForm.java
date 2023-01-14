package com.andrewsha.marketplace.domain.product_card.request;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.andrewsha.marketplace.domain.product.request.CreateProductForm;

public class CreateProductCardForm {
    @NotBlank(message = "name cannot be empty or null")
    @Size(min = 3, max = 20,
            message = "product name must be greater than 3 but less than 20 characters")
    private String name;

    @Size(min = 10, max = 50,
            message = "short description must be greater than 10 but less than 50 characters")
    private String shortDescription;

    @Size(min = 10, max = 100,
            message = "description must be greater than 10 but less than 100 characters")
    private String description;

    @Valid
    @NotEmpty(message = "set of products cannot be empty")
    private Set<CreateProductForm> products = new HashSet<>();

    @NotBlank(message = "category cannot be empty or null")
    private String category;

    @NotNull(message = "store cannot be null")
    private UUID store;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CreateProductForm> getProducts() {
		return products;
	}

	public void setProducts(Set<CreateProductForm> products) {
		this.products = products;
	}

	public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public UUID getStore() {
        return store;
    }

    public void setStore(UUID store) {
        this.store = store;
    }
}

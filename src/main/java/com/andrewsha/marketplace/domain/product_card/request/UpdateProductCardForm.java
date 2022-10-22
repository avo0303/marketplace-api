package com.andrewsha.marketplace.domain.product_card.request;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.andrewsha.marketplace.domain.product.Product;

public class UpdateProductCardForm {
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
    private Set<Product> products = new HashSet<>();

    private String category;

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

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

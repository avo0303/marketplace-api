package com.andrewsha.marketplace.domain.product.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.andrewsha.marketplace.domain.product.image.ProductImage;

public class UpdateProductForm {
    @Size(min = 3, max = 20,
            message = "product name must be greater than 3 but less than 20 characters")
    private String name;

    @Min(value = 1, message = "price must be greater than 1")
    private Double actualPrice;

    @Min(value = 0, message = "discount must be equal or greater than 0")
    @Max(value = 99, message = "discount must be equal or less than 99")
    private Double discount;

    private List<ProductImage> images;

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

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, String> attributes) {
        this.attributes = attributes;
    }
}

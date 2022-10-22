package com.andrewsha.marketplace.domain.product.image;

import java.util.ArrayList;
import java.util.List;

public class ProductImageList {
    private List<ProductImage> productImages = new ArrayList<>();

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }
}

package com.andrewsha.marketplace.domain.product.resource;

import com.andrewsha.marketplace.domain.product.image.ProductImage;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiType;

public class ProductImageDTO {
    @JsonApiId
    private final String id;
    @JsonApiType
    private final String type = "product-image";
    private final String url;

    public ProductImageDTO(ProductImage image) {
        this.id = image.getId().toString();
        this.url = image.getUrl();
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}

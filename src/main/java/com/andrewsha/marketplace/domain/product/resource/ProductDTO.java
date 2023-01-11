package com.andrewsha.marketplace.domain.product.resource;

import java.util.Map;
import com.andrewsha.marketplace.domain.product.Product;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiType;

public class ProductDTO {
    @JsonApiId
    private final String id;
    @JsonApiType
    private final String type = "product";
    private final String name;
    private final String actualPrice;
    private final String discount;
    private final Map<String, String> productAttributes;

    public ProductDTO(Product product) {
        this.id = product.getId() != null ? product.getId().toString() : null;
        this.name = product.getName();
        this.actualPrice =
                product.getActualPrice() != null ? product.getActualPrice().toString()
                        : null;
        this.discount =
                product.getDiscount() != null ? product.getDiscount().toString() : null;
        this.productAttributes = product.getAttributes();
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public Map<String, String> getProductAttributes() {
        return productAttributes;
    }
}

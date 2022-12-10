package com.andrewsha.marketplace.domain.store.resource;

import com.andrewsha.marketplace.domain.store.Store;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiType;

public class StoreDTO {

    @JsonApiId
    private final String id;
    @JsonApiType
    private final String type = "store";
    private final String name;
    private final String description;

    public StoreDTO(Store store) {
        this.id = store.getId() != null ? store.getId().toString() : null;
        this.name = store.getName();
        this.description = store.getDescription();
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

    public String getDescription() {
        return description;
    }
}

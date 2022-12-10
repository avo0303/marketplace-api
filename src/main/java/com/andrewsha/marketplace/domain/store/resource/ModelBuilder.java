package com.andrewsha.marketplace.domain.store.resource;

import com.andrewsha.marketplace.domain.store.Store;
import com.andrewsha.marketplace.utils.DomainObjectModelBuilder;
import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.RepresentationModel;

public class ModelBuilder implements DomainObjectModelBuilder<Store> {

	@Override
	public RepresentationModel<?> build(Store store) {
		StoreDTO dto = new StoreDTO(store);
		// TODO relations
		return JsonApiModelBuilder.jsonApiModel().model(dto).build();
	}

	@Override
	public RepresentationModel<?> build(Page<Store> page) {
		// TODO Auto-generated method stub
		return null;
	}
}

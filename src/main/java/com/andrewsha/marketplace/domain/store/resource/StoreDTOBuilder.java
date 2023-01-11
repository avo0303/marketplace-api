package com.andrewsha.marketplace.domain.store.resource;

import java.util.Collection;
import java.util.stream.Collectors;
import com.andrewsha.marketplace.domain.store.Store;
import com.andrewsha.marketplace.utils.DTOBuilder;
import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class StoreDTOBuilder implements DTOBuilder<Store> {
	@Value(value = "${api.endpoint.store}")
	private String endpoint;

	@Override
	public RepresentationModel<?> build(Store store) {
		return JsonApiModelBuilder.jsonApiModel().model(new StoreDTO(store))
				.link(Link.of(ServletUriComponentsBuilder.fromCurrentContextPath()
						.replacePath(endpoint + "/" + store.getId().toString()).toUriString()))
				.build();
	}

	@Override
	public RepresentationModel<?> build(Page<Store> page) {
		Collection<RepresentationModel<?>> contentCollection =
				page.getContent().stream().map(e -> this.build(e)).collect(Collectors.toList());
		PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(page.getSize(),
				page.getNumber(), page.getTotalElements(), page.getTotalPages());
		Link selfLink = Link.of(ServletUriComponentsBuilder.fromCurrentContextPath()
				.replacePath(endpoint).toUriString()).withSelfRel();
		PagedModel<RepresentationModel<?>> pagedModel =
				PagedModel.of(contentCollection, metadata, selfLink);
		return JsonApiModelBuilder.jsonApiModel().model(pagedModel).pageMeta()
				.pageLinks(ServletUriComponentsBuilder.fromCurrentContextPath()
						.replacePath(endpoint).toUriString())
				.build();
	}
}

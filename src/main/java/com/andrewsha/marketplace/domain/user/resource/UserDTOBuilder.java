package com.andrewsha.marketplace.domain.user.resource;

import java.util.Collection;
import java.util.stream.Collectors;
import com.andrewsha.marketplace.domain.user.User;
import com.andrewsha.marketplace.utils.DTOBuilder;
import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class UserDTOBuilder implements DTOBuilder<User> {
	@Value(value = "${api.endpoint.user}")
	private String endpoint;

	@Override
	public RepresentationModel<?> build(User source) {
		return JsonApiModelBuilder.jsonApiModel().model(new UserDTO(source))
				.link(Link.of(ServletUriComponentsBuilder.fromCurrentContextPath()
						.replacePath(endpoint + "/" + source.getId().toString()).toUriString()))
				.build();
	}

	@Override
	public RepresentationModel<?> build(Page<User> page) {
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

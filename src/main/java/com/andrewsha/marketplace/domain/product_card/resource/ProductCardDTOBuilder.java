package com.andrewsha.marketplace.domain.product_card.resource;

import java.util.Collection;
import java.util.stream.Collectors;
import com.andrewsha.marketplace.domain.product.resource.ProductDTO;
import com.andrewsha.marketplace.domain.product_card.ProductCard;
import com.andrewsha.marketplace.domain.store.resource.StoreDTO;
import com.andrewsha.marketplace.utils.DTOBuilder;
import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class ProductCardDTOBuilder implements DTOBuilder<ProductCard> {
	@Value(value = "${api.endpoint.product-card}")
	private String endpoint;

	@Override
	public RepresentationModel<?> build(ProductCard productCard) {
		Collection<ProductDTO> productResources = productCard.getProducts().stream()
				.map(e -> new ProductDTO(e)).collect(Collectors.toList());
		StoreDTO storeResource = new StoreDTO(productCard.getStore());
		return JsonApiModelBuilder.jsonApiModel().model(new ProductCardDTO(productCard))
				.link(Link.of(ServletUriComponentsBuilder.fromCurrentContextPath()
						.replacePath(endpoint + "/" + productCard.getId().toString())
						.toUriString()))
				.relationship("products", productResources).relationship("store", storeResource)
				.included(productResources).included(storeResource).build();
	}

	@Override
	public RepresentationModel<?> build(Page<ProductCard> page) {
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
		// TODO add included products and stores
	}
}

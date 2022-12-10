package com.andrewsha.marketplace.domain.product_card.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import com.andrewsha.marketplace.domain.product.resource.ProductDTO;
import com.andrewsha.marketplace.domain.product_card.ProductCard;
import com.andrewsha.marketplace.domain.store.resource.StoreDTO;
import com.andrewsha.marketplace.utils.DomainObjectModelBuilder;
import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

@Component
public class ModelBuilder implements DomainObjectModelBuilder<ProductCard> {

	@Override
	public RepresentationModel<?> build(ProductCard productCard) {
		Collection<ProductDTO> productResources = productCard.getProducts().stream()
				.map(e -> new ProductDTO(e)).collect(Collectors.toList());
		StoreDTO storeResource = new StoreDTO(productCard.getStore());
		return JsonApiModelBuilder.jsonApiModel().model(new ProductCardDTO(productCard))
				.relationship("products", productResources).relationship("store", storeResource)
				.included(productResources).included(storeResource).build();
	}

	@Override
	public RepresentationModel<?> build(Page<ProductCard> page) {
		Collection<ProductDTO> products = new ArrayList<>();
		Collection<StoreDTO> stores = new ArrayList<>();
		for(ProductCard productCard : page.getContent()){
				
		}
		Collection<RepresentationModel<?>> contentCollection =
				page.getContent().stream().map(e -> new ProductCardDTO(e)).collect(Collectors.toList());
		PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(page.getSize(),
				page.getNumber(), page.getTotalElements(), page.getTotalPages());
		// TODO other meta
		// TODO hardcode
		Link selfLink = Link.of("http://localhost:8080/product-card").withSelfRel();
		PagedModel<RepresentationModel<?>> pagedModel =
				PagedModel.of(contentCollection, metadata, selfLink);
		return JsonApiModelBuilder.jsonApiModel().model(pagedModel).pageMeta()
				.pageLinks("http://localhost:8080/product-card").included().build();
		// TODO complete and test
	}
}

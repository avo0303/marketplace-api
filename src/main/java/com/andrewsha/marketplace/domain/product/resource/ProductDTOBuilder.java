package com.andrewsha.marketplace.domain.product.resource;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import com.andrewsha.marketplace.domain.product.Product;
import com.andrewsha.marketplace.utils.DTOBuilder;
import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class ProductDTOBuilder implements DTOBuilder<Product> {
    @Value(value = "${api.endpoint.product}")
    private String endpoint;

    @Override
    public RepresentationModel<?> build(Product source) {
        List<ProductImageDTO> images = source.getImages().stream().map(e -> new ProductImageDTO(e))
                .collect(Collectors.toList());
        return JsonApiModelBuilder.jsonApiModel().model(new ProductDTO(source))
                .link(Link.of(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .replacePath(endpoint + "/" + source.getId().toString()).toUriString()))
                .relationship("images", images).included(images).build();
    }

    @Override
    public RepresentationModel<?> build(Page<Product> page) {
        Collection<RepresentationModel<?>> contentCollection =
                page.getContent().stream().map(e -> this.build(e)).collect(Collectors.toList());
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(page.getSize(),
                page.getNumber(), page.getTotalElements(), page.getTotalPages());
        Link selfLink = Link.of(ServletUriComponentsBuilder.fromCurrentContextPath()
                .replacePath(endpoint).toUriString()).withSelfRel();
        PagedModel<RepresentationModel<?>> pagedModel =
                PagedModel.of(contentCollection, metadata, selfLink);

        //TODO check that correct
        Collection<ProductImageDTO> images =
                page.getContent().stream().map(e -> e.getImages()).flatMap(e -> e.stream())
                        .map(e -> new ProductImageDTO(e)).collect(Collectors.toSet());

        return JsonApiModelBuilder.jsonApiModel().model(pagedModel).included(images).pageMeta()
                .pageLinks(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .replacePath(endpoint).toUriString())
                .build();
    }
}

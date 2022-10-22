package com.andrewsha.marketplace.domain.product;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, UUID> {
}

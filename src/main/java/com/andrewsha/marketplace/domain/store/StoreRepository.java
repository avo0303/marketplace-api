package com.andrewsha.marketplace.domain.store;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface StoreRepository extends PagingAndSortingRepository<Store, UUID> {

}

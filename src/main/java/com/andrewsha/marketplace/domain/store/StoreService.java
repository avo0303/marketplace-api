package com.andrewsha.marketplace.domain.store;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andrewsha.marketplace.domain.product_card.ProductCard;
import com.andrewsha.marketplace.domain.user.User;
import com.andrewsha.marketplace.domain.user.authority.AuthorityEnum;
import com.andrewsha.marketplace.domain.user.authority.AuthorityService;
import com.andrewsha.marketplace.domain.user.authority.AuthoritySet;
import com.andrewsha.marketplace.exception.StoreServiceException;

@Service
public class StoreService {
	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private AuthorityService authorityService;

	public Page<Store> getStores(int page, int size) {
		// TODO: sort by rating?
		return this.storeRepository.findAll(PageRequest.of(page, size));
	}

	public Store getStore(UUID id) {
		return this.storeRepository.findById(id).orElseThrow(
				() -> new StoreServiceException("store with id " + id + " does not exists"));
	}

	@Transactional
	public Store createStore(Store storeBody, User authenticatedUser) {
		Store store = this.storeRepository.save(storeBody);

		AuthoritySet permissionSet = new AuthoritySet();
		permissionSet.setScope(store.getId().toString());
		permissionSet
				.addAuthorities(this.authorityService.getPermissions(
						Stream.of(AuthorityEnum.CREATE, AuthorityEnum.READ, AuthorityEnum.UPDATE,
								AuthorityEnum.DELETE).collect(Collectors.toSet()),
						ProductCard.class));
		permissionSet
				.addAuthorities(
						this.authorityService.getPermissions(
								Stream.of(AuthorityEnum.READ, AuthorityEnum.UPDATE,
										AuthorityEnum.DELETE).collect(Collectors.toSet()),
								Store.class));
		permissionSet.addUser(authenticatedUser);
		this.authorityService.save(permissionSet);
		return store;
	}

	public void deleteStore(UUID id) {
		this.storeRepository.deleteById(id);
	}
}

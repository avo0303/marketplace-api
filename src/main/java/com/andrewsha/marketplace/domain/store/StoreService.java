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
import com.andrewsha.marketplace.domain.user.permission.PermissionEnum;
import com.andrewsha.marketplace.domain.user.permission.PermissionService;
import com.andrewsha.marketplace.domain.user.permission.PermissionSet;
import com.andrewsha.marketplace.exception.StoreServiceException;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private PermissionService permissionService;

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

        PermissionSet permissionSet = new PermissionSet();
        permissionSet.setScope(store.getId().toString());
        permissionSet
                .addPermissions(this.permissionService.getPermissions(
                        Stream.of(PermissionEnum.CREATE, PermissionEnum.READ, PermissionEnum.UPDATE,
                                PermissionEnum.DELETE).collect(Collectors.toSet()),
                        ProductCard.class));
        permissionSet
                .addPermissions(
                        this.permissionService.getPermissions(
                                Stream.of(PermissionEnum.READ, PermissionEnum.UPDATE,
                                        PermissionEnum.DELETE).collect(Collectors.toSet()),
                                Store.class));
        permissionSet.addUser(authenticatedUser);
        this.permissionService.save(permissionSet);
        return store;
    }

    public void deleteStore(UUID id) {
        this.storeRepository.deleteById(id);
    }
}

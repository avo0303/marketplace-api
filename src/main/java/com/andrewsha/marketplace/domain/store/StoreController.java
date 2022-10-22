package com.andrewsha.marketplace.domain.store;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andrewsha.marketplace.domain.user.User;

@RestController
@RequestMapping(path = "api/v1/store")
@Validated
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<?> getStores(@RequestParam @Min(0) int page,
            @RequestParam @Min(1) int size) {
        Page<Store> storesPage = this.storeService.getStores(page, size);
        return ResponseEntity.ok(storesPage);
    }

    @GetMapping(path = "/{storeId}")
    public ResponseEntity<?> getStore(@PathVariable("storeId") UUID id) {
        return ResponseEntity.ok(this.storeService.getStore(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<?> createStore(@Valid @RequestBody Store store,
            Authentication authentication) {
        return ResponseEntity
                .ok(this.storeService.createStore(store, (User) authentication.getPrincipal()));
    }

    @DeleteMapping(path = "/{storeId}")
    @PreAuthorize("hasPermission(#id, 'Store', 'DELETE')")
    public ResponseEntity<?> deleteStore(@PathVariable("storeId") UUID id) {
        this.storeService.deleteStore(id);
        return ResponseEntity.ok("store with id " + id + " successfully deleted");
    }
    // TODO complete
}

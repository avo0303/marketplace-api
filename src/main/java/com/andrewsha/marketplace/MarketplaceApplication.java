package com.andrewsha.marketplace;

import com.andrewsha.marketplace.domain.product_card.ProductCard;
import com.andrewsha.marketplace.domain.user.permission.Permission;
import com.andrewsha.marketplace.domain.user.permission.PermissionEnum;
import com.andrewsha.marketplace.domain.user.permission.PermissionRepository;
import com.andrewsha.marketplace.domain.user.permission.PermissionSetRepository;
import com.andrewsha.marketplace.domain.user.role.Role;
import com.andrewsha.marketplace.domain.user.role.RoleEnum;
import com.andrewsha.marketplace.domain.user.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class MarketplaceApplication implements CommandLineRunner {
    @Autowired
    PermissionSetRepository userPermissionRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(MarketplaceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        this.initRoles();
        this.initBaseSellersPermissions();
    }

    private void initRoles() {
        if (!this.roleRepository.existsByName(RoleEnum.ROLE_USER)) {
            this.roleRepository.save(new Role(RoleEnum.ROLE_USER));
        }
        if (!this.roleRepository.existsByName(RoleEnum.ROLE_SELLER)) {
            this.roleRepository.save(new Role(RoleEnum.ROLE_SELLER));
        }
        if (!this.roleRepository.existsByName(RoleEnum.ROLE_ADMIN)) {
            this.roleRepository.save(new Role(RoleEnum.ROLE_ADMIN));
        }
        if (!this.roleRepository.existsByName(RoleEnum.ROLE_SUPER_ADMIN)) {
            this.roleRepository.save(new Role(RoleEnum.ROLE_SUPER_ADMIN));
        }
    }

    private void initBaseSellersPermissions() {
        if (!this.permissionRepository.existsByName("CREATE_PRODUCTCARD")) {
            this.permissionRepository
                    .save(new Permission(PermissionEnum.CREATE, ProductCard.class));
        }
        if (!this.permissionRepository.existsByName("UPDATE_PRODUCTCARD")) {
            this.permissionRepository
                    .save(new Permission(PermissionEnum.UPDATE, ProductCard.class));
        }
        if (!this.permissionRepository.existsByName("DELETE_PRODUCTCARD")) {
            this.permissionRepository
                    .save(new Permission(PermissionEnum.DELETE, ProductCard.class));
        }
    }
}

package com.andrewsha.marketplace;

import com.andrewsha.marketplace.domain.product_card.ProductCard;
import com.andrewsha.marketplace.domain.user.authority.Authority;
import com.andrewsha.marketplace.domain.user.authority.AuthorityEnum;
import com.andrewsha.marketplace.domain.user.authority.AuthorityRepository;
import com.andrewsha.marketplace.domain.user.authority.AuthoritySetRepository;
import com.andrewsha.marketplace.domain.user.authority.Role;
import com.andrewsha.marketplace.domain.user.authority.RoleEnum;
import com.andrewsha.marketplace.domain.user.authority.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class MarketplaceApplication implements CommandLineRunner {
	@Autowired
	AuthoritySetRepository userPermissionRepository;

	@Autowired
	AuthorityRepository permissionRepository;

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
			this.permissionRepository.save(new Authority(AuthorityEnum.CREATE, ProductCard.class));
		}
		if (!this.permissionRepository.existsByName("UPDATE_PRODUCTCARD")) {
			this.permissionRepository.save(new Authority(AuthorityEnum.UPDATE, ProductCard.class));
		}
		if (!this.permissionRepository.existsByName("DELETE_PRODUCTCARD")) {
			this.permissionRepository.save(new Authority(AuthorityEnum.DELETE, ProductCard.class));
		}
	}
}

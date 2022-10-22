package com.andrewsha.marketplace.domain.user.permission;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    public Optional<Permission> findByName(String name);

    public boolean existsByName(String name);
}

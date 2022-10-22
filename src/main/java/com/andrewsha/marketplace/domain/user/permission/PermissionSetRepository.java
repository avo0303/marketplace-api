package com.andrewsha.marketplace.domain.user.permission;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionSetRepository extends JpaRepository<PermissionSet, UUID> {
    public List<PermissionSet> findByScope(String scope);
}

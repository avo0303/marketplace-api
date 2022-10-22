package com.andrewsha.marketplace.security.authority;

import org.springframework.security.core.GrantedAuthority;

import com.andrewsha.marketplace.domain.user.permission.Permission;
import com.andrewsha.marketplace.domain.user.role.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CustomGrantedAuthority implements GrantedAuthority {
    private final RoleEnum role;
    private final String scope;
    private final String permission;

    private static final long serialVersionUID = 3709905622125984076L;

    @Override
    public String getAuthority() {
        if (this.role != null) {
            return this.role.toString().toUpperCase();
        } else {
            if (this.permission != null && this.scope != null) {
                return "PERMISSION_" + this.permission.toUpperCase() + "_" + this.scope;
            }
        }
        return null;
    }

    public CustomGrantedAuthority(Permission permission, String scope) {
        super();
        this.role = null;
        this.scope = scope;
        this.permission = permission.getName();
    }

    public CustomGrantedAuthority(RoleEnum role) {
        super();
        this.role = role;
        this.scope = null;
        this.permission = null;
    }

    @JsonIgnore
    public RoleEnum getRole() {
        return role;
    }

    @JsonIgnore
    public String getScope() {
        return scope;
    }

    @JsonIgnore
    public String getPermission() {
        return permission;
    }
}

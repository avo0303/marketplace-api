package com.andrewsha.marketplace.security.authority;

import com.andrewsha.marketplace.domain.user.authority.Authority;
import com.andrewsha.marketplace.domain.user.authority.RoleEnum;
import org.springframework.security.core.GrantedAuthority;

public class CustomGrantedAuthority implements GrantedAuthority {
    private final RoleEnum role;
    private final String scope;
    private final String authorityName;

    private static final long serialVersionUID = 3709905622125984076L;

    @Override
    public String getAuthority() {
        if (this.role != null) {
            return this.role.toString().toUpperCase();
        } else {
            if (this.authorityName!= null && this.scope != null) {
                return "PERMISSION_" + this.authorityName.toUpperCase() + "_" + this.scope;
            }
        }
        return null;
    }

    public CustomGrantedAuthority(Authority authority, String scope) {
        super();
        this.role = null;
        this.scope = scope;
        this.authorityName = authority.getName();
    }

    public CustomGrantedAuthority(RoleEnum role) {
        super();
        this.role = role;
        this.scope = null;
        this.authorityName = null;
    }

	public RoleEnum getRole() {
		return role;
	}

	public String getScope() {
		return scope;
	}

	public String getAuthorityName() {
		return authorityName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

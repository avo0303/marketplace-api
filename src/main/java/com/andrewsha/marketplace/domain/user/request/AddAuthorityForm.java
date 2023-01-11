package com.andrewsha.marketplace.domain.user.request;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;

public class AddAuthorityForm {
    Long authorityId;
    String authorityName;
    @NotEmpty(message = "scope cannot be empty")
    String scope;

    @AssertTrue(message = "authority id or name needed")
    private boolean isPermissionIdOrNameExists() {
        return this.authorityId != null || this.authorityName != null;
    }

	public Long getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(Long authorityId) {
		this.authorityId = authorityId;
	}

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

}

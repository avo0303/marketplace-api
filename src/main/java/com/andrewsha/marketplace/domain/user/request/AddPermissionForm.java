package com.andrewsha.marketplace.domain.user.request;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;

public class AddPermissionForm {
    Long permissionId;
    String permissionName;
    @NotEmpty(message = "scope cannot be empty")
    String scope;

    @AssertTrue(message = "permission id or name needed")
    private boolean isPermissionIdOrNameExists() {
        return this.permissionId != null || this.permissionName != null;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}

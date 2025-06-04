package com.example.backendqlks.entity.compositekeys;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
public class RolePermissionPrimaryKey implements Serializable {
    private int userRoleId;
    private int permissionId;

    public RolePermissionPrimaryKey(int userRoleId, int permissionId) {
        this.userRoleId = userRoleId;
        this.permissionId = permissionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof RolePermissionPrimaryKey other) {
            return (this.userRoleId==other.userRoleId) && (this.permissionId==other.permissionId);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(userRoleId, permissionId);
    }
}

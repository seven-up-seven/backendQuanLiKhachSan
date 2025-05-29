package com.example.backendqlks.entity.compositekeys;

import com.example.backendqlks.entity.RolePermission;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
public class RolePermissionPrimaryKey implements Serializable {
    private int roleId;
    private int permissionId;

    public RolePermissionPrimaryKey(int roleId, int permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof RolePermissionPrimaryKey other) {
            return (this.roleId==other.roleId) && (this.permissionId==other.permissionId);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(roleId, permissionId);
    }
}

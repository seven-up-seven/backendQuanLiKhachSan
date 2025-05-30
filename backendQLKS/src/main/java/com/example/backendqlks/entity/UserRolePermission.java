package com.example.backendqlks.entity;

import com.example.backendqlks.entity.compositekeys.RolePermissionPrimaryKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="ROLE_PERMISSION")
@Data
@IdClass(RolePermissionPrimaryKey.class)
public class UserRolePermission {
    @Id
    @Column(name="USER_ROLE_ID")
    private int userRoleId;

    @Id
    @Column(name = "PERMISSION_ID")
    private int permissionId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ROLE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private UserRole userRole;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "ID", updatable = false, insertable = false)
    private Permission permission;
}

package com.genersoft.iot.vmp.storager.dao.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;

public class RoleAddItem {
    @ApiModelProperty(value = "该角色拥有得权限，数组[]", required = true)
    private ArrayList permissions;
    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public ArrayList getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList permissions) {
        this.permissions = permissions;
    }
}


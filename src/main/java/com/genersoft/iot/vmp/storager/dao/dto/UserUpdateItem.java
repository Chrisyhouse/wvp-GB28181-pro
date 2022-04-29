package com.genersoft.iot.vmp.storager.dao.dto;

import io.swagger.annotations.ApiModelProperty;

public class UserUpdateItem {
    @ApiModelProperty(value = "用户ID", required = true)
    private int userId;
    @ApiModelProperty(value = "角色ID", required = true)
    private int roleId;
    @ApiModelProperty(value = "昵称", required = true)
    private String nickname;
    @ApiModelProperty(value = "软删除1:未删 2：已删", required = true)
    private String deleteStatus;

    public String getNickname() {
        return nickname;
    }

    public int getRoleId() {
        return roleId;
    }

    public int getUserId() {
        return userId;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

}

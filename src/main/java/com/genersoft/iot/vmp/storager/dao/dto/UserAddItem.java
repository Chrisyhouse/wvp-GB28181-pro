package com.genersoft.iot.vmp.storager.dao.dto;

public class UserAddItem {

    private String  username;
    private String  password;
    private String  nickname;
    private int  roleId;

    public String getUsername() {
        return username;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

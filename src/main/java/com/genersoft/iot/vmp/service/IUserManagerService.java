package com.genersoft.iot.vmp.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: hxy
 * @description: 用户/角色/权限
 * @date: 2017/11/2 10:18
 */
public interface IUserManagerService {
    /**
     * 用户列表
     */
    JSONObject listUser(JSONObject jsonObject);

    /**
     * 查询所有的角色
     * 在添加/修改用户的时候要使用此方法
     */
    JSONObject getAllRoles();

    /**
     * 添加用户
     */
    JSONObject addUser(JSONObject jsonObject);

    /**
     * 修改用户
     */
    JSONObject updateUser(JSONObject jsonObject);


    /**
     * 查询所有权限, 给角色分配权限时调用
     */
    JSONObject listAllPermission();

    /**
     * 新增权限
     */

    JSONObject addPermission(String menu_name);

    /**
     * 修改权限
     */
    JSONObject updatePermission(String old_menu_name,String new_menu_name);

    /**
     * 删除权限
     */
    JSONObject deletePermission(String menu_name);

    /**
     * 角色列表
     */
    JSONObject listRole();

    /**
     * 添加角色
     */
    JSONObject addRole(JSONObject jsonObject);

    /**
     * 修改角色
     */
    JSONObject updateRole(JSONObject jsonObject);

    /**
     * 删除角色
     */
    JSONObject deleteRole(JSONObject jsonObject);
}

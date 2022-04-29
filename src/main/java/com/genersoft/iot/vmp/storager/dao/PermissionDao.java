package com.genersoft.iot.vmp.storager.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.Set;

/**
 * @author: hxy
 * @date: 2017/10/30 13:28
 */
public interface PermissionDao {
	/**
	 * 查询用户的角色 菜单 权限
	 */
	JSONObject getUserPermission(String username);

	/**
	 * 查询用户的设备组
	 */
	JSONObject getUserDeviceGp(String username);

	/**
	 * 查询所有的菜单
	 */
	Set<String> getAllMenu();

	/**
	 * 查询所有的权限
	 */
	Set<String> getAllPermission();

	/**
	 * 查询设备组下所有设备
	 */
	Set<String> getAllDevicesByDeviceGp(String menuName);

	/**
	 * 查询角色下下所有设备
	 */
	Set<String> getAllDevicesByRoleIdAndMenuName(int roleId , String menuName);
}

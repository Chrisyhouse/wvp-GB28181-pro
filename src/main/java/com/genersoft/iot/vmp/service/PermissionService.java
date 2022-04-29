package com.genersoft.iot.vmp.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: hxy
 * @date: 2017/10/30 13:10
 */
public interface PermissionService {
	/**
	 * 查询某用户的 角色  菜单列表   权限列表
	 */
	JSONObject getUserPermission(String username);

	/**
	 * 查询某用户的 设备组（权限）
	 */
	JSONObject getUserDeviceGP(String username);

	/**
	 * 查询某设备组（权限）下的设备列表
	 */
	public JSONObject getDeviceListByDeviceGp(String menuName);

}

package com.genersoft.iot.vmp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.genersoft.iot.vmp.storager.dao.PermissionDao;
import com.genersoft.iot.vmp.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author: xwy
 * @date: 2021/04/28 13:15
 */
@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionDao permissionDao;

	/**
	 * 查询某用户的 角色  菜单列表   权限列表
	 */
	@Override
	public JSONObject getUserPermission(String username) {
		JSONObject userPermission = getUserPermissionFromDB(username);
		return userPermission;
	}

	/**
	 * 查询某用户的 设备组（权限）
	 */
	@Override
	public JSONObject getUserDeviceGP(String username) {
		JSONObject userPermission = permissionDao.getUserDeviceGp(username);
		return userPermission;
	}

	/**
	 * 查询某设备组（权限）下的设备列表
	 */
	@Override
	public JSONObject getDeviceListByDeviceGp(String menuName) {
		Set<String> deviceList = permissionDao.getAllDevicesByDeviceGp(menuName);
		JSONObject deviceListJsObj = new JSONObject();
		deviceListJsObj.put("deviceList",deviceList);
		return deviceListJsObj;
	}

	/**
	 * 从数据库查询用户权限信息
	 */
	private JSONObject getUserPermissionFromDB(String username) {
		JSONObject userPermission = permissionDao.getUserPermission(username);
		//管理员角色ID为1
		int adminRoleId = 1;
		//如果是管理员
		String roleIdKey = "roleId";
		if (adminRoleId == userPermission.getIntValue(roleIdKey)) {
			//查询所有菜单  所有权限
			Set<String> menuList = permissionDao.getAllMenu();
			Set<String> permissionList = permissionDao.getAllPermission();
			userPermission.put("menuList", menuList);
			userPermission.put("permissionList", permissionList);
		}
		return userPermission;
	}

}

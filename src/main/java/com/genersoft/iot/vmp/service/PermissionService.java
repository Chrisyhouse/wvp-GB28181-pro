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

}

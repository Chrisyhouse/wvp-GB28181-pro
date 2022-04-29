package com.genersoft.iot.vmp.storager.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: xwy
 * @description: 用户/角色/权限
 * @date: 2021-04-28 15:08:45
 */
public interface UserManagerDao {
	/**
	 * 查询用户数量
	 */
	int countUser(JSONObject jsonObject);

	/**
	 * 查询用户列表
	 */
	List<JSONObject> listUser(JSONObject jsonObject);

	/**
	 * 查询所有的角色
	 * 在添加/修改用户的时候要使用此方法
	 */
	List<JSONObject> getAllRoles();

	/**
	 * 校验用户名是否已存在
	 */
	int queryExistUsername(JSONObject jsonObject);

	/**
	 * 新增用户
	 */
	int addUser(JSONObject jsonObject);

	/**
	 * 修改用户
	 */
	int updateUser(JSONObject jsonObject);

	/**
	 * 查询所有权限, 给角色分配权限时调用
	 */
	List<JSONObject> listAllPermission();
	/**
	 * 查询是否存在重复名称的List
	 */
	List<JSONObject> listRepeatPermission(String menu_name);

	/**
	 * 新增权限
	 */
	int insertPermission(JSONObject jsonObject);

	/**
	 * 更新权限表参数
	 */
	int updatePermissionMenuName(String old_menu_name,String new_menu_name);

	/**
	 * 根据名称删除权限表
	 */
	int RemovePermissionByMenuName(String menu_name);

	/**
	 * 根据perimission_id删除权限表
	 */
	int deleteRolePermission(int id);

	/**
	 * 批量插入权限的设备组
	 *
	 * @param id      角色ID
	 * @param devices 权限
	 */
	int insertDevicePermission(@Param("id") String id, @Param("devices") List<Integer> devices);

	/**
	 * 角色列表
	 */
	List<JSONObject> listRole();

	/**
	 * 新增角色
	 */
	int insertRole(JSONObject jsonObject);

	/**
	 * 批量插入角色的权限
	 *
	 * @param roleId      角色ID
	 * @param permissions 权限
	 */
	int insertRolePermission(@Param("roleId") String roleId, @Param("permissions") List<Integer> permissions);

	/**
	 * 将角色曾经拥有而修改为不再拥有的权限 delete_status改为'2'
	 */
	int removeOldPermission(@Param("roleId") String roleId, @Param("permissions") List<Integer> permissions);

	/**
	 * 修改角色名称
	 */
	int updateRoleName(JSONObject jsonObject);

	/**
	 * 查询某角色的全部数据
	 * 在删除和修改角色时调用
	 */
	JSONObject getRoleAllInfo(JSONObject jsonObject);

	/**
	 * 删除角色
	 */
	int removeRole(JSONObject jsonObject);

	/**
	 * 删除本角色全部权限
	 */
	int removeRoleAllPermission(JSONObject jsonObject);

	/**
	 * 删除角色权限表下的所有某个权限
	 */
	int removeOnePermission(@Param("permission_id") String permission_id);
}

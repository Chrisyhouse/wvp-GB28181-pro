package com.genersoft.iot.vmp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.service.IUserManagerService;
import com.genersoft.iot.vmp.storager.IVideoManagerStorager;
import com.genersoft.iot.vmp.storager.dao.UserManagerDao;
import com.genersoft.iot.vmp.utils.CommonUtil;
import com.genersoft.iot.vmp.utils.constants.ErrorEnum;
import org.junit.platform.commons.function.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author: hxy
 * @description: 用户/角色/权限
 * @date: 2017/11/2 10:18
 */
@Service
public class UserManagerServiceImpl implements IUserManagerService {
    @Autowired
    private UserManagerDao userDao;

    @Autowired
    private IVideoManagerStorager storager;

    /**
     * 用户列表
     */
    @Override
    public JSONObject listUser(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = userDao.countUser(jsonObject);
        List<JSONObject> list = userDao.listUser(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 添加用户
     */
    @Override
    public JSONObject addUser(JSONObject jsonObject) {
        int exist = userDao.queryExistUsername(jsonObject);
        if (exist > 0) {
            return CommonUtil.errorJson(ErrorEnum.E_10009);
        }
        JSONObject data = new JSONObject();
        userDao.addUser(jsonObject);
        data.put("result", "success");
        return CommonUtil.successJson(data);
    }

    /**
     * 查询所有的角色
     * 在添加/修改用户的时候要使用此方法
     */
    @Override
    public JSONObject getAllRoles() {
        List<JSONObject> roles = userDao.getAllRoles();
        return CommonUtil.successPage(roles);
    }

    /**
     * 修改用户
     */
    @Override
    public JSONObject updateUser(JSONObject jsonObject) {
        userDao.updateUser(jsonObject);
        return CommonUtil.successJson();
    }


    /**
     * 查询所有权限, 给角色分配权限时调用
     */
    @Override
    public JSONObject listAllPermission() {
        List<JSONObject> permissions = userDao.listAllPermission();
        return CommonUtil.successPage(permissions);
    }

    /**
     * 新增权限菜单设备组名称
     */
    @Override
    public JSONObject addPermission(String menu_name) {
       //非空
       if(menu_name != null){
           JSONObject requestJson = new JSONObject();
           List<Device> deviceList =  storager.queryVideoDeviceList();
           //判断是否重复
           List<JSONObject>  listRepeatPermission = userDao.listRepeatPermission(menu_name);
           if(listRepeatPermission.size()>0){
               return CommonUtil.errorJson(ErrorEnum.E_10011);
           }else {
               try {
                   for (int i = 0; i < deviceList.size(); i++) {
                       Device device = deviceList.get(i);
                       requestJson.put("id" , (int)((Math.random()*9+1)*100000));//生成6位随机数（不会是5位或者7位，仅只有6位）：
                       requestJson.put("menu_code" , "deviceGp");
                       requestJson.put("menu_name" , menu_name);
                       requestJson.put("permission_code" , device.getDeviceId());
                       requestJson.put("permission_name" , device.getName());
                       requestJson.put("required_permission" , 2);
                       userDao.insertPermission(requestJson);
                   }
                   return CommonUtil.successJson();
               }catch (Exception e){
                   return CommonUtil.errorJson(ErrorEnum.E_400);
               }
           }
       }else {
           return CommonUtil.errorJson(ErrorEnum.E_400);
       }
    }

    /**
     * 修改权限菜单设备组名称
     */
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    @Override
    public JSONObject updatePermission(String old_menu_name,String new_menu_name) {
        //非空
        if(old_menu_name != null && new_menu_name != null ){
            //判断新名称是否重复
            List<JSONObject>  listRepeatPermission = userDao.listRepeatPermission(new_menu_name);
            if(listRepeatPermission.size()>0){
                return CommonUtil.errorJson(ErrorEnum.E_10011);
            }else {
                try {
                    //更新设备组名称
                    userDao.updatePermissionMenuName(old_menu_name,new_menu_name);
                    return CommonUtil.successJson();
                }catch (Exception e){
                    return CommonUtil.errorJson(ErrorEnum.E_400);
                }
            }
        }else {
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }

    /**
     * 删除权限菜单
     */
    @Override
    public JSONObject deletePermission(String menu_name) {
        try{
            //删除角色权限关系表中的所有权限
            List<JSONObject> RepeatPermissionList = userDao.listRepeatPermission(menu_name);
            if(RepeatPermissionList.size()>0){
                for (int i = 0; i < RepeatPermissionList.size(); i++) {
                    int id = Integer.parseInt(RepeatPermissionList.get(i).getJSONArray("permissions").getJSONObject(0).getString("id"));
                    userDao.deleteRolePermission(id);
                }
            }
            //删除权限表
            userDao.RemovePermissionByMenuName(menu_name);
            return CommonUtil.successJson();
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }

    }


    /**
     * 角色列表
     */
    @Override
    public JSONObject listRole() {
        List<JSONObject> roles = userDao.listRole();
        return CommonUtil.successPage(roles);
    }

    /**
     * 添加角色
     */
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    @Override
    public JSONObject addRole(JSONObject jsonObject) {
        userDao.insertRole(jsonObject);
        userDao.insertRolePermission(jsonObject.getString("roleId"), (List<Integer>) jsonObject.get("permissions"));
        return CommonUtil.successJson();
    }

    /**
     * 修改角色
     */
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    @Override
    public JSONObject updateRole(JSONObject jsonObject) {
        String roleId = jsonObject.getString("roleId");
        List<Integer> newPerms = (List<Integer>) jsonObject.get("permissions");
        JSONObject roleInfo = userDao.getRoleAllInfo(jsonObject);
        Set<Integer> oldPerms = (Set<Integer>) roleInfo.get("permissionIds");
        //修改角色名称
        dealRoleName(jsonObject, roleInfo);
        //添加新权限
        saveNewPermission(roleId, newPerms, oldPerms);
        //移除旧的不再拥有的权限
        removeOldPermission(roleId, newPerms, oldPerms);
        return CommonUtil.successJson();
    }

    /**
     * 修改角色名称
     */
    private void dealRoleName(JSONObject paramJson, JSONObject roleInfo) {
        String roleName = paramJson.getString("roleName");
        if (!roleName.equals(roleInfo.getString("roleName"))) {
            userDao.updateRoleName(paramJson);
        }
    }

    /**
     * 为角色添加新权限
     */
    private void saveNewPermission(String roleId, Collection<Integer> newPerms, Collection<Integer> oldPerms) {
        List<Integer> waitInsert = new ArrayList<>();
        for (Integer newPerm : newPerms) {
            if (!oldPerms.contains(newPerm)) {
                waitInsert.add(newPerm);
            }
        }
        if (waitInsert.size() > 0) {
            userDao.insertRolePermission(roleId, waitInsert);
        }
    }

    /**
     * 删除角色 旧的 不再拥有的权限
     */
    private void removeOldPermission(String roleId, Collection<Integer> newPerms, Collection<Integer> oldPerms) {
        List<Integer> waitRemove = new ArrayList<>();
        for (Integer oldPerm : oldPerms) {
            if (!newPerms.contains(oldPerm)) {
                waitRemove.add(oldPerm);
            }
        }
        if (waitRemove.size() > 0) {
            userDao.removeOldPermission(roleId, waitRemove);
        }
    }

    /**
     * 删除角色
     */
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    @Override
    public JSONObject deleteRole(JSONObject jsonObject) {
        JSONObject roleInfo = userDao.getRoleAllInfo(jsonObject);
        List<JSONObject> users = (List<JSONObject>) roleInfo.get("users");
        if (users != null && users.size() > 0) {
            return CommonUtil.errorJson(ErrorEnum.E_10008);
        }
        userDao.removeRole(jsonObject);
        userDao.removeRoleAllPermission(jsonObject);
        return CommonUtil.successJson();
    }
}

package com.genersoft.iot.vmp.vmanager.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.genersoft.iot.vmp.annotation.Log;
import com.genersoft.iot.vmp.conf.security.SecurityUtils;
import com.genersoft.iot.vmp.conf.security.dto.LoginUser;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.media.zlm.dto.MediaServerItem;
import com.genersoft.iot.vmp.service.IRoleService;
import com.genersoft.iot.vmp.service.IUserManagerService;
import com.genersoft.iot.vmp.service.IUserService;
import com.genersoft.iot.vmp.service.PermissionService;
import com.genersoft.iot.vmp.storager.dao.dto.*;
import com.genersoft.iot.vmp.utils.CommonUtil;
import com.genersoft.iot.vmp.vmanager.bean.LoginResult;
import com.genersoft.iot.vmp.vmanager.bean.WVPResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;

@Api(tags = "用户角色权限管理")
@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private IUserManagerService userManagerService;

    @Autowired
    private IRoleService roleService;

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ApiOperation("登录-测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "用户名", dataTypeClass = String.class),
            @ApiImplicitParam(name = "password", required = true, value = "密码（32位md5加密）", dataTypeClass = String.class),
    })
    @GetMapping("/login")
    public WVPResult<LoginUser> login(@RequestParam String username, @RequestParam String password){
        LoginUser user = null;
        WVPResult<LoginUser> result = new WVPResult<>();
        try {
            user = SecurityUtils.login(username, password, authenticationManager);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            result.setCode(-1);
            result.setMsg("fail");
        }
        if (user != null) {
            result.setCode(0);
            result.setMsg("success");
            result.setData(user);
        }else {
            result.setCode(-1);
            result.setMsg("fail");
        }
        return result;
    }

    @Log("用户登录")
    @ApiOperation("登录[正式]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataTypeClass = String.class,required = true),
            @ApiImplicitParam(name = "password", value = "密码（测试123456）", dataTypeClass = String.class,required = true),
    })
    @GetMapping("/auth")
    public LoginResult auth(String username, String password) {
        LoginUser user;
        LoginResult<Object> result = new LoginResult<>();
        JSONObject resultData = new JSONObject();
        try {
            user = SecurityUtils.login(username, password, authenticationManager);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            result.setCode(404);
            result.setMsg("登录出错。");
            resultData.put("result","error");
            result.setData(resultData);
            return result;
        }
        if (user != null) {

            result.setCode(200);
            result.setMsg("登录成功。");
            resultData.put("result","success");
            result.setData(resultData);
            return result;
        } else {
            result.setCode(403);
            result.setMsg("帐户或密码不正确。");
            resultData.put("result","incorrect");
            result.setData(resultData);
            return result;
        }

    }

    /* 获取用户信息 */
//    @Log("获取用户信息")
    @ApiOperation("获取用户信息")
    @GetMapping("/getUserInfo")
    public WVPResult GetUserInfo() {
        WVPResult<Object> result = new WVPResult<>();
        result.setCode(200);
        result.setMsg("success");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        JSONObject userPermission = permissionService.getUserPermission(username);
        JSONObject resultData = new JSONObject();
        resultData.put("userPermission", userPermission);
        resultData.put("username", username);
        resultData.put("avatar", "https://i.gtimg.cn/club/item/face/img/2/15922_100.gif");

        result.setData(resultData);
        return result;
    }




    /* 用户登出 */
    @Log("用户登出")
    @ApiOperation("用户登出")
    @GetMapping("/logout")
    public String logout() {
        return "success";
    }

//    /* 修改密码 */
//    @ApiOperation("修改密码")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "username", value = "用户名", dataTypeClass = String.class,required = false),
//            @ApiImplicitParam(name = "oldpassword", value = "旧密码（已md5加密的密码）", dataTypeClass = String.class,required = true),
//            @ApiImplicitParam(name = "password", value = "新密码（未md5加密的密码）", dataTypeClass = String.class,required = true),
//    })
//    @PostMapping("/changePassword")
//    public WVPResult changePassword(String oldpassword, String password) {
//        WVPResult<Object> cpResult = new WVPResult<>();
//        // 获取当前登录用户id
//        String username = SecurityUtils.getUserInfo().getUsername();
//        LoginUser user = null;
//        try {
//            user = SecurityUtils.login(username, oldpassword, authenticationManager);
//            if (user != null) {
//                int userId = SecurityUtils.getUserId();
//                boolean result = userService.changePassword(userId, DigestUtils.md5DigestAsHex(password.getBytes()));
//                if (result) {
//                    cpResult.setCode(200);
//                    cpResult.setMsg("success");
//                    return  cpResult;
//                }
//            }
//        } catch (AuthenticationException e) {
//            e.printStackTrace();
//        }
//        cpResult.setCode(403);
//        cpResult.setMsg("fail");
//        return  cpResult;
//    }

    /**
     * 查询用户列表
     */
//    @RequiresPermissions("user:list")
//    @Log("查询用户列表")
    @ApiOperation("查询用户列表")
    @GetMapping("/list")
    public JSONObject listUser(HttpServletRequest request) {
        return userManagerService.listUser(CommonUtil.request2Json(request));
    }



    /**
     * 新增用户
     */
//    @RequiresPermissions("user:add")
//    @Log("新增用户")
    @ApiOperation("新增用户")
    @PostMapping("/addUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userAddItem", value = "新增用户", dataTypeClass = UserAddItem.class)
    })
    public JSONObject addUser(@RequestBody UserAddItem userAddItem) {
        JSONObject requestJson = JSON.parseObject(JSON.toJSONString(userAddItem));
        CommonUtil.hasAllRequired(requestJson, "username, password, nickname,  roleId");
        return userManagerService.addUser(requestJson);
    }

    /**
     * 更新用户
     */
//    @RequiresPermissions("user:update")
//    @Log("更新用户")
    @ApiOperation("更新用户")
    @PostMapping("/updateUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userUpdateItem", value = "更新用户", dataTypeClass = UserUpdateItem.class)
    })
    public JSONObject updateUser(@RequestBody UserUpdateItem userUpdateItem) {
        JSONObject requestJson = JSON.parseObject(JSON.toJSONString(userUpdateItem));
        CommonUtil.hasAllRequired(requestJson, " nickname,   roleId, deleteStatus, userId");
        return userManagerService.updateUser(requestJson);
    }

    //    @RequiresPermissions(value = {"user:add", "user:update"}, logical = Logical.OR)
//    @Log("获取所有角色")
    @ApiOperation("获取所有角色")
    @GetMapping("/getAllRoles")
    public JSONObject getAllRoles() {
        return userManagerService.getAllRoles();
    }



    /**
     * 查询所有权限, 给角色分配权限时调用
     */
//    @RequiresPermissions("role:list")
//    @Log("查询所有权限")
    @ApiOperation("查询所有权限")
    @GetMapping("/listAllPermission")
    public JSONObject listAllPermission() {
        return userManagerService.listAllPermission();
    }



    /**
     *     @Log("用户登录")
     *     @ApiOperation("登录[正式]")
     *     @ApiImplicitParams({
     *             @ApiImplicitParam(name = "username", value = "用户名", dataTypeClass = String.class,required = true),
     *             @ApiImplicitParam(name = "password", value = "密码（测试123456）", dataTypeClass = String.class,required = true),
     *     })
     *     @GetMapping("/auth")
     *     public LoginResult auth(String username, String password) {
     */

    /**
     * 新增设备组名（权限）
     */
    @Log("新增用户设备组名（权限）")
    @ApiOperation("新增用户设备组名（权限）")
    @GetMapping("/addPermission")
    @ApiImplicitParams({
                         @ApiImplicitParam(value = "菜单名称", name = "menu_name", required = true, dataType = "String",
                                 paramType = "query")
                  })
    public JSONObject addPermission(String menu_name) {
        return userManagerService.addPermission(menu_name);
    }


    /**
     * 修改设备组名（权限）
     */
    @Log("修改用户设备组名（权限）")
    @ApiOperation("修改用户设备组名（权限）")
    @GetMapping("/updatePermission")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "原设备组名", name = "old_menu_name", required = true, dataType = "String",
                    paramType = "query"),
            @ApiImplicitParam(value = "新设备组名", name = "new_menu_name", required = true, dataType = "String",
                    paramType = "query")
    })
    public JSONObject updatePermission(String old_menu_name,String new_menu_name) {
        return userManagerService.updatePermission(old_menu_name,new_menu_name);
    }

    /**
     * 删除用户设备组名（权限）
     */
    @Log("删除用户设备组名（权限）")
    @ApiOperation("删除用户设备组名（权限）")
    @GetMapping("/deletePermission")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "设备组名", name = "menu_name", required = true, dataType = "String",
                    paramType = "query")
    })
    public JSONObject deletePermission(String menu_name) {
        return userManagerService.deletePermission(menu_name);
    }

    /**
     * 查询用户设备组名（权限）
     */
    @Log("查询用户设备组名（权限）")
    @ApiOperation("查询用户设备组名（权限）")
    @GetMapping("/getUserGeviceGP")
    public WVPResult GetUserGeviceGP() {
        WVPResult<Object> result = new WVPResult<>();
        result.setCode(200);
        result.setMsg("success");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        JSONObject userPermission = permissionService.getUserDeviceGP(username);
        JSONObject resultData = new JSONObject();
        resultData.put("userDeviceGp", userPermission);
        resultData.put("username", username);

        result.setData(resultData);
        return result;
    }



    /**
     * 角色列表
     */
//    @RequiresPermissions("role:list")
//    @Log("角色列表")
    @ApiOperation("角色列表")
    @GetMapping("/listRole")
    public JSONObject listRole() {
        return userManagerService.listRole();
    }

    /**
     * 新增角色
     */
    @Log("新增角色")
    @ApiOperation("新增角色")
    @PostMapping("/addRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name="roleAddItem", value = "新增角色信息", dataTypeClass = RoleAddItem.class)
    })
    public JSONObject addRole(@RequestBody RoleAddItem roleAddItem) {
        JSONObject requestJson = JSON.parseObject(JSON.toJSONString(roleAddItem));
        CommonUtil.hasAllRequired(requestJson, "roleName,permissions");
        return userManagerService.addRole(requestJson);
    }

    /**
     * 修改角色
     */
//    @RequiresPermissions("role:update")
    @Log("修改角色")
    @ApiOperation("修改角色")
    @PostMapping("/updateRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name="roleUpdateItem", value = "修改角色信息", dataTypeClass = RoleUpdateItem.class)
    })
    public JSONObject updateRole(@RequestBody RoleUpdateItem roleUpdateItem) {
        JSONObject requestJson = JSON.parseObject(JSON.toJSONString(roleUpdateItem));
        CommonUtil.hasAllRequired(requestJson, "roleId,roleName,permissions");
        return userManagerService.updateRole(requestJson);
    }

    /**
     * 删除角色
     */
//    @RequiresPermissions("role:delete")
    @Log("删除角色")
    @ApiOperation("删除角色")
    @PostMapping("/deleteRole")
    public JSONObject deleteRole(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "roleId");
        return userManagerService.deleteRole(requestJson);
    }

//    @ApiOperation("修改密码")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "username", required = true, value = "用户名", dataTypeClass = String.class),
//            @ApiImplicitParam(name = "oldpassword", required = true, value = "旧密码（已md5加密的密码）", dataTypeClass = String.class),
//            @ApiImplicitParam(name = "password", required = true, value = "新密码（未md5加密的密码）", dataTypeClass = String.class),
//    })
//    @PostMapping("/changePassword")
//    public String changePassword(@RequestParam String oldPassword, @RequestParam String password){
//        // 获取当前登录用户id
//        LoginUser userInfo = SecurityUtils.getUserInfo();
//        if (userInfo== null) {
//            return "fail";
//        }
//        String username = userInfo.getUsername();
//        LoginUser user = null;
//        try {
//            user = SecurityUtils.login(username, oldPassword, authenticationManager);
//            if (user != null) {
//                int userId = SecurityUtils.getUserId();
//                boolean result = userService.changePassword(userId, DigestUtils.md5DigestAsHex(password.getBytes()));
//                if (result) {
//                    return "success";
//                }
//            }
//        } catch (AuthenticationException e) {
//            e.printStackTrace();
//        }
//        return "fail";
//    }


//    @ApiOperation("添加用户")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "username", required = true, value = "用户名", dataTypeClass = String.class),
//            @ApiImplicitParam(name = "password", required = true, value = "密码（未md5加密的密码）", dataTypeClass = String.class),
//            @ApiImplicitParam(name = "roleId", required = true, value = "角色ID", dataTypeClass = String.class),
//    })
//    @PostMapping("/add")
//    public ResponseEntity<WVPResult<Integer>> add(@RequestParam String username,
//                                                 @RequestParam String password,
//                                                 @RequestParam Integer roleId){
//        WVPResult<Integer> result = new WVPResult<>();
//        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || roleId == null) {
//            result.setCode(-1);
//            result.setMsg("参数不可为空");
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
//        // 获取当前登录用户id
//        int currenRoleId = SecurityUtils.getUserInfo().getRole().getId();
//        if (currenRoleId != 1) {
//            // 只用角色id为1才可以删除和添加用户
//            result.setCode(-1);
//            result.setMsg("用户无权限");
//            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
//        }
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
//
//        Role role = roleService.getRoleById(roleId);
//
//        if (role == null) {
//            result.setCode(-1);
//            result.setMsg("roleId is not found");
//            // 角色不存在
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        }
//        user.setRole(role);
//        user.setCreateTime(format.format(System.currentTimeMillis()));
//        user.setUpdateTime(format.format(System.currentTimeMillis()));
//        int addResult = userService.addUser(user);
//
//        result.setCode(addResult > 0 ? 0 : -1);
//        result.setMsg(addResult > 0 ? "success" : "fail");
//        result.setData(addResult);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    @ApiOperation("删除用户")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", required = true, value = "用户Id", dataTypeClass = Integer.class),
//    })
//    @DeleteMapping("/delete")
//    public ResponseEntity<WVPResult<String>> delete(@RequestParam Integer id){
//        // 获取当前登录用户id
//        int currenRoleId = SecurityUtils.getUserInfo().getRole().getId();
//        WVPResult<String> result = new WVPResult<>();
//        if (currenRoleId != 1) {
//            // 只用角色id为0才可以删除和添加用户
//            result.setCode(-1);
//            result.setMsg("用户无权限");
//            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
//        }
//        int deleteResult = userService.deleteUser(id);
//
//        result.setCode(deleteResult>0? 0 : -1);
//        result.setMsg(deleteResult>0? "success" : "fail");
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    @ApiOperation("查询用户")
//    @ApiImplicitParams({})
//    @GetMapping("/all")
//    public ResponseEntity<WVPResult<List<User>>> all(){
//        // 获取当前登录用户id
//        List<User> allUsers = userService.getAllUsers();
//        WVPResult<List<User>> result = new WVPResult<>();
//        result.setCode(0);
//        result.setMsg("success");
//        result.setData(allUsers);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
}

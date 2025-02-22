//package com.genersoft.iot.vmp.storager.dao;
//
//import com.genersoft.iot.vmp.storager.dao.dto.User;
//import org.apache.ibatis.annotations.*;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Mapper
//@Repository
//public interface UserMapper {
//
//    @Insert("INSERT INTO user (username, password, roleId, createTime, updateTime) VALUES" +
//            "('${username}', '${password}', '${role.id}', '${createTime}', '${updateTime}')")
//    int add(User user);
//
//    @Update(value = {" <script>" +
//            "UPDATE user " +
//            "SET updateTime='${updateTime}' " +
//            "<if test=\"role != null\">, roleId='${role.id}'</if>" +
//            "<if test=\"password != null\">, password='${password}'</if>" +
//            "<if test=\"username != null\">, username='${username}'</if>" +
//            "WHERE id=#{id}" +
//            " </script>"})
//    int update(User user);
//
//    @Delete("DELETE FROM user WHERE id != 1 and id=#{id}")
//    int delete(int id);
//
//    @Select("select u.*, r.id as roleID, r.name as roleName, r.authority as roleAuthority , r.createTime as roleCreateTime , r.updateTime as roleUpdateTime FROM user u, user_role r WHERE u.roleId=r.id and u.username=#{username} AND u.password=#{password}")
//    @Results(id = "roleMap", value = {
//            @Result(column = "roleID", property = "role.id"),
//            @Result(column = "roleName", property = "role.name"),
//            @Result(column = "roleAuthority", property = "role.authority"),
//            @Result(column = "roleCreateTime", property = "role.createTime"),
//            @Result(column = "roleUpdateTime", property = "role.updateTime")
//    })
//    User select(String username, String password);
//
//    @Select("select u.*, r.id as roleID, r.name as roleName, r.authority as roleAuthority , r.createTime as roleCreateTime , r.updateTime as roleUpdateTime FROM user u, user_role r WHERE u.roleId=r.id and u.id=#{id}")
//    @ResultMap(value="roleMap")
//    User selectById(int id);
//
//    @Select("select u.*, r.id as roleID, r.name as roleName, r.authority as roleAuthority , r.createTime as roleCreateTime , r.updateTime as roleUpdateTime FROM user u, user_role r WHERE u.roleId=r.id and u.username=#{username}")
//    @ResultMap(value="roleMap")
//    User getUserByUsername(String username);
//
//    @Select("select u.*, r.id as roleID, r.name as roleName, r.authority as roleAuthority , r.createTime as roleCreateTime , r.updateTime as roleUpdateTime FROM user u, user_role r WHERE u.roleId=r.id")
//    @ResultMap(value="roleMap")
//    List<User> selectAll();
//}
package com.genersoft.iot.vmp.storager.dao;

import com.genersoft.iot.vmp.storager.dao.dto.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    @Insert("INSERT INTO sys_user (username, password, role_id, create_time) VALUES" +
            "('${username}', '${password}', '${role_id}', datetime('now','localtime'))")
    int add(User user);

    @Update("UPDATE sys_user " +
            "SET username=#{username}," +
            "password=#{password}," +
            "role_id=#{role_id}" +
            "WHERE id=#{id}")
    int update(User user);

    @Delete("DELETE FROM sys_user WHERE app=#{app} AND id=#{id}")
    int delete(User user);

    @Select("select * FROM sys_user WHERE username= #{username} AND password=#{password}")
    User select(String username, String password);

    @Select("select * FROM sys_user WHERE id= #{id}")
    User selectById(int id);

    @Select("select * FROM sys_user WHERE username= #{username}")
    User getUserByUsername(String username);
}


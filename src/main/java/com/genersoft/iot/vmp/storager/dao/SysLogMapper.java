package com.genersoft.iot.vmp.storager.dao;

import com.genersoft.iot.vmp.storager.dao.dto.SysLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用于存储操作日志
 * author:xwy 2021/5/28 15:55
 *
 */
@Mapper
@Repository
public interface SysLogMapper {
    /***
     * 新增操作日志
     * @param sysLog
     * @return
     */
    @Insert("INSERT INTO sys_log (username, operation, time, method, params, ip, createTime) VALUES" +
            "('${username}', '${operation}', '${time}', '${method}','${params}','${ip}','${createTime}' )")
    int add(SysLog sysLog);

    /***
     * 分页查询操作日志
     * @return
     */
    @Select("SELECT * FROM sys_log ORDER BY id DESC")
    List<SysLog> getSysLogs();

}

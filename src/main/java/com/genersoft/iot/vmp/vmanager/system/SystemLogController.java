package com.genersoft.iot.vmp.vmanager.system;

import com.genersoft.iot.vmp.annotation.Log;
import com.genersoft.iot.vmp.gb28181.bean.DeviceAlarm;
import com.genersoft.iot.vmp.gb28181.bean.DeviceChannel;
import com.genersoft.iot.vmp.gb28181.transmit.callback.RequestMessage;
import com.genersoft.iot.vmp.storager.IRedisCatchStorage;
import com.genersoft.iot.vmp.storager.dao.dto.SysLog;
//import com.genersoft.iot.vmp.vmanager.zc.controller.DeviceGet;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import com.alibaba.fastjson.JSONObject;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.event.DeviceOffLineDetector;
import com.genersoft.iot.vmp.gb28181.transmit.callback.DeferredResultHolder;
import com.genersoft.iot.vmp.gb28181.transmit.cmd.impl.SIPCommander;
import com.genersoft.iot.vmp.storager.IVideoManagerStorager;

import javax.sip.message.Response;

@Api(tags = "系统", value = "系统")
@SuppressWarnings("rawtypes")
@CrossOrigin
@RestController
@RequestMapping("/api/system")
public class SystemLogController {
//    private final static Logger logger = LoggerFactory.getLogger(DeviceGet.class);

    @Autowired
    private IVideoManagerStorager storager;


    /**
     * 分页查询操作日志
     * @param page 当前页
     * @param count 每页查询数量
     * @return 分页操作日志
     */
    @Log("分页查询操作日志")
    @ApiOperation("分页查询操作日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "count", value = "每页查询数量", required = true, dataTypeClass = Integer.class),
    })
    @GetMapping("/syslog")
    public PageInfo<SysLog> SysLogs(int page, int count){

//        if (logger.isDebugEnabled()) {
//            logger.debug("分页查询操作日志");
//        }

        return storager.querySysLogList(page, count);
    }

}

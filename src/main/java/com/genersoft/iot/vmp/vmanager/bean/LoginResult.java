package com.genersoft.iot.vmp.vmanager.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class LoginResult<T> {

    @ApiModelProperty(value = "返回状态码",example = "200",required = true)
    private int code;
    @ApiModelProperty(value = "返回信息",example = "登录成功。", required = true)
    private String msg;
    @ApiModelProperty(value = "返回数据,忽略转义字符",example = "{\"result\":\"success\"}",required = true)
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}

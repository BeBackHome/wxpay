package com.cloudolp.peony.server.wxpay.support;

/**
 * Created by chao.zeng on 2016/10/14.
 */
public enum Error {
    AUTH_FAIL("认证失败"),
    NOT_FOUND("请求资源不存在"),
    PARAMS_ERROR("参数错误"),
    NEVER_MEASURE("没有测脚"),
    UNKONWN("未知错误");
    public final String msg;
    Error(String msg){
        this.msg=msg;
    }
}

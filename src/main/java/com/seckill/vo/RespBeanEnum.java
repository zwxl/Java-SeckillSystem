package com.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public enum RespBeanEnum {
    //通用状态码
    SUCCESS(200,"success"),
    ERROR(500,"服务端异常"),
    //登录模块5002xx
    SESSION_ERROR(500210,"session不存在或者已经失效"),
    LOGINVO_ERROR(500211,"用户名或者密码错误"),
    REQUEST_ILLEGAL(500250,"手机号码格式错误"),
    //注册模块
    REGISTER_ERROR(500213,"注册失败"),
    MOBILE_ERROR(500212,"手机号码格式错误");
    //秒杀模块
    private final Integer code;
    private final String message;
}

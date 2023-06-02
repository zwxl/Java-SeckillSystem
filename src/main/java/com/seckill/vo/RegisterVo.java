package com.seckill.vo;

import lombok.Data;

@Data
public class RegisterVo {
    private String phonenumber;
    private String idcardnumber;
    private String name;
    private String password;
    private int age;
}

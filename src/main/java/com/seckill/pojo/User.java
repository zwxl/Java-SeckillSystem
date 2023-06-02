package com.seckill.pojo;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@ToString
@Getter
@Slf4j
public class User {
    private String phonenumber;
    private String idcardnumber;
    private String name;
    private String password;
    private String integrity;
    private int age;
    private double deposit;
}

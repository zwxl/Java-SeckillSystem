package com.seckill.dao;

import com.seckill.pojo.User;

public interface UserDao {
    /*
        用户的注册
        将注册的用户的数据插入数据库
     */
    Boolean register(User user);
    String loginValidate(String phonenumber);
}

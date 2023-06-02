package com.seckill.service;

import com.seckill.pojo.User;
import com.seckill.vo.LoginVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    boolean registeruser(User user);//
    boolean loginvalidate(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);
    LoginVo getUserByCookie(String userticket,HttpServletRequest request,HttpServletResponse response);
}

package com.seckill.service;

import com.seckill.pojo.Product;
import com.seckill.pojo.User;
import com.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface OrderService {

    String kill(Integer id);
    RespBean testkill(User user, Integer id, HttpServletRequest request, HttpServletResponse response);
    //获取秒杀地址
    String createPath(User tuser);
    //检验秒杀地址
    boolean checkPath(User user, String path);
}

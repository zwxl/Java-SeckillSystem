package com.seckill.service;

import com.seckill.dao.UserDao;
import com.seckill.pojo.User;
import com.seckill.random.DepositRandom;
import com.seckill.random.IntegrityRandom;
import com.seckill.utils.CookieUtil;
import com.seckill.utils.UUIDUtil;
import com.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public boolean registeruser(User user) {
        user.setDeposit(DepositRandom.IsDeposit());
        user.setIntegrity(IntegrityRandom.Isintegrity());
        Boolean aBoolean = userDao.register(user);
        if(aBoolean)
        return true;
        else{
            return false;
        }
    }

    @Override
    public boolean loginvalidate(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String s = userDao.loginValidate(loginVo.getPhonenumber());
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //判断重复

        String ticket = UUIDUtil.uuid();
        valueOperations.set("user"+ticket,loginVo);
        System.out.println("ticket"+valueOperations.get("user"+ticket));
        CookieUtil.setCookie(request,response,"userticket",ticket);
        if(loginVo.getPassword()==null||loginVo.getPhonenumber()==null||loginVo.getPhonenumber().length()!=11||s==null){
            return false;
        }
            return true;
    }

    @Override
    public LoginVo getUserByCookie(String userticket,HttpServletRequest request,HttpServletResponse response) {
        if(userticket==null)
            return null;
        ValueOperations valueOperations = redisTemplate.opsForValue();
        LoginVo loginVo = (LoginVo) valueOperations.get("user" + userticket);
        System.out.println(loginVo);
        if(loginVo!=null){
            CookieUtil.setCookie(request,response,"userticket",userticket);
        }
        return  loginVo;
    }
}

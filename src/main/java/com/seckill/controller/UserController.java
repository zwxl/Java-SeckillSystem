package com.seckill.controller;
import com.seckill.dao.UserDao;
import com.seckill.pojo.User;
import com.seckill.service.UserService;
import com.seckill.vo.LoginVo;
import com.seckill.vo.RegisterVo;
import com.seckill.vo.RespBean;
import com.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/user")
@Slf4j
@CrossOrigin
public class UserController {
    //登录功能
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping("/dologin")
    @ResponseBody
    //500:登陆失败
    //200:登陆成功
    public RespBean dologin(LoginVo loginVo,HttpServletRequest request,HttpServletResponse response){

        boolean loginvalidate = userService.loginvalidate(loginVo,request,response);
        if(!loginvalidate)
            return RespBean.error(RespBeanEnum.ERROR);
        return RespBean.success();
    }
    //注册用户
    @PostMapping("/register")
    @ResponseBody
    public RespBean register(RegisterVo registerVo){
        User user = new User();
        user.setAge(registerVo.getAge());
        user.setIdcardnumber(registerVo.getIdcardnumber());
        user.setName(registerVo.getName());
        user.setPassword(registerVo.getPassword());
        user.setPhonenumber(registerVo.getPhonenumber());
        String s = userDao.loginValidate(user.getPhonenumber());
//        if(s==null){
//            return RespBean.error(RespBeanEnum.REGISTER_ERROR);
//        }
            if( userService.registeruser(user)||registerVo.getAge()<18){
                System.out.println("ERROR");
                return RespBean.error(RespBeanEnum.REGISTER_ERROR);
            }
            System.out.println("SUCCESS");
            return RespBean.success();
    }
}

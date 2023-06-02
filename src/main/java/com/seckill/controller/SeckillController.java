package com.seckill.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.seckill.dao.OrderDao;
import com.seckill.pojo.Product;
import com.seckill.pojo.User;
import com.seckill.service.OrderService;
import com.seckill.service.UserService;
import com.seckill.utils.UUIDUtil;
import com.seckill.vo.LoginVo;
import com.seckill.vo.RespBean;
import com.seckill.vo.RespBeanEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@CrossOrigin
public class SeckillController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private  RedisTemplate redisTemplate;
    @Autowired
    private OrderDao orderDao;
    @GetMapping("/ms/{path}")
    public String kill(@PathVariable String path,Integer id,User user){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        boolean check = orderService.checkPath(user,path);
        if (!check) {
//            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
            return "fail";
        }
        String order = orderService.kill(id);
        try{
            System.out.println("订单id="+order);
            return "秒杀成功，订单id为： "+order;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("秒杀失败");
            return e.getMessage();
        }

    }
    private RateLimiter rateLimiter = RateLimiter.create(1000);
//    测试redis和cookie
    @RequestMapping("/testkill")
    @ResponseBody                                                                  //@CookieValue("userticket") String ticket)
    public RespBean testkill(User user, Integer id, HttpServletRequest request, HttpServletResponse response){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Product product = orderDao.checksale(id);
        System.out.println(product.getSale());
        valueOperations.setIfAbsent("sale",product.getSale());
        /*测试redis存储cookies*/
//        LoginVo userByCookie = userService.getUserByCookie(ticket, request, response);
//        if(userByCookie==null){
//            return RespBean.error(RespBeanEnum.ERROR);
//        }
        /* =================*/
        /*
            和redis中的setnx等同
         */
//        valueOperations.setIfAbsent()
        /*
            cookie在java中的使用
         */
        //将库存信息存入redis中
        String clientId = UUIDUtil.uuid();
        System.out.println(clientId);
        try{
//            Boolean aBoolean = valueOperations.setIfAbsent(user.getPhonenumber(),clientId);
           valueOperations.set(user.getPhonenumber(),clientId);
//            System.out.println(aBoolean);
//            if(aBoolean){
                rateLimiter.acquire();
//                if(!rateLimiter.tryAcquire(2, TimeUnit.SECONDS)){
//                    System.out.println("当前请求被限流,直接抛弃,无法调用后续秒杀逻辑....");
                    orderService.testkill(user,id,request,response);
//                }
//            }else{
//                return RespBean.error(RespBeanEnum.ERROR);
//            }
        }finally {
            if(clientId.equals(valueOperations.get(user.getPhonenumber())))
            redisTemplate.delete(user.getPhonenumber());
        }
        return RespBean.success();
    }

    //秒杀接口的隐藏
    @ApiOperation("获取秒杀地址")
    @GetMapping(value = "/path")
    @ResponseBody
    public RespBean  getPath(User tuser,String captcha, HttpServletRequest request) {
        if (tuser == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

//
//    boolean check = orderService.checkCaptcha(tuser, goodsId, captcha);
//    if (!check) {
//        return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
//    }
        String str = orderService.createPath(tuser);
        return RespBean.success(str);
    }
}


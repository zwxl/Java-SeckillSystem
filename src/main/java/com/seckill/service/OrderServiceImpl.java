package com.seckill.service;

import com.alibaba.druid.util.StringUtils;
import com.google.common.util.concurrent.RateLimiter;
import com.seckill.dao.OrderDao;
import com.seckill.pojo.Orderlist;
import com.seckill.pojo.Product;
import com.seckill.pojo.SeckillMessage;
import com.seckill.pojo.User;
import com.seckill.rabbitmq.MQsender;
import com.seckill.utils.JsonUtil;
import com.seckill.utils.MD5Util;
import com.seckill.utils.UUIDUtil;
import com.seckill.vo.RespBean;
import com.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MQsender mQsender;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String kill(Integer id) {
        Product product = orderDao.checksale(id);
        User user = new User();
        user.setPhonenumber("15573936106");
        //判断库存
        if(product.getSale().equals(product.getCount())){
            throw new RuntimeException("库存不足");
        }

        product.setSale(product.getSale()+1);
            orderDao.updatesale(product);
            Orderlist orderlist = new Orderlist();
            orderlist.setOrderid(product.getProductid());
            orderlist.setOrdertime(new Date());
            orderDao.createorder(orderlist);
//            System.out.println(product.getProductid());
            return null;
    }

    @Override
    public RespBean testkill(User user, Integer id, HttpServletRequest request, HttpServletResponse response) {
        Product product = orderDao.checksale(id);
//        redis缓存判断是否已经卖完
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer sale = (Integer) valueOperations.get("sale");
        if(sale<product.getCount()){
//            Long sale1 = valueOperations.increment("sale");
            System.out.println("redis预减库存："+valueOperations.increment("sale"));
            //rabbitmq发送消息
            SeckillMessage seckillMessage = new SeckillMessage(user, product.getProductid());
            mQsender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
            return RespBean.success(RespBeanEnum.SUCCESS);
        }
        else{
            System.out.println("库存已满，秒杀失败");
             return RespBean.error(RespBeanEnum.ERROR);
        }

    }

    @Override
    public String createPath(User user) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:" + user.getPhonenumber(), str, 1, TimeUnit.MINUTES);
        return str;
    }

    @Override
    public boolean checkPath(User user, String path) {
        if (user == null  || StringUtils.isEmpty(path)) {
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getPhonenumber());
        return path.equals(redisPath);
    }

}

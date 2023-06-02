package com.seckill.rabbitmq;

import com.seckill.dao.OrderDao;
import com.seckill.pojo.Orderlist;
import com.seckill.pojo.Product;
import com.seckill.pojo.SeckillMessage;
import com.seckill.pojo.User;
import com.seckill.service.OrderService;
import com.seckill.utils.JsonUtil;
import com.seckill.vo.RespBean;
import com.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderDao orderDao;

    @RabbitListener(queues = "seckillQueue")
    public void receive(String message){
        log.info("接收消息：" + message);
//        MessageProperties properties = message.getMessageProperties();
//        // 消息内容，二进制数据
//        byte[] body = message.getBody();
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Integer productid = seckillMessage .getProductid();
        User user = seckillMessage.getUser();
        Product product = orderDao.checksale(productid);
        //判断是否库存已满
        if(product.getSale()==(product.getCount())){
            return ;
        }

        System.out.println("更新库存"+product.getSale());
//        product.setSale(product.getSale()+1);
//        int updatesale = orderDao.updatesale(product);
        orderDao.updatesale1(product);
        System.out.println("更新后的数据"+product.getSale());
//        if(updatesale==0){
//            return ;
//        }
        Orderlist orderlist = new Orderlist();
        orderlist.setOrderid(product.getProductid());
        orderlist.setOrdertime(new Date());
        orderlist.setName(user.getName());
        orderDao.createorder(orderlist);
        System.out.println("消息发送成功");
        return;

    }


}
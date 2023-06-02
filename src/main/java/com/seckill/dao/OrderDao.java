package com.seckill.dao;

import com.seckill.pojo.Orderlist;
import com.seckill.pojo.Product;


public interface OrderDao {
    Product checksale(Integer productid);

    int updatesale(Product product);
    int updatesale1(Product product);
    void createorder(Orderlist orderlist);
}

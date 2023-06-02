package com.seckill.pojo;

import lombok.Data;

@Data
public class Product {
    private Integer productid;
    private double price;
    private Integer sale;
    private double rate;
    private Integer count;
    private Integer version;
}

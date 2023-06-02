package com.seckill.random;

import java.util.Random;
/*
    用户诚信度判断
    并通过Random函数生成诚信度
 */
public class IntegrityRandom {
    public static String Isintegrity(){
        Random random = new Random();
        int anInt = random.nextInt(3);
        if(anInt==1||anInt==3){
            return "yes";
        }
        else{
            return "no";
        }

    }
}

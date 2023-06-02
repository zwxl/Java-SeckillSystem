package com.seckill.random;

import java.util.Random;
/*
    生成存款随机数
    判断存款余额是否可以参加这个秒杀活动
 */
//生成存款随机数
public class DepositRandom {
    public static double IsDeposit(){
        Random random2 = new Random();
        Random rand = new Random();
        double MAX=6.88;
        double MIN=0.68;
        double result=0;
        int count = random2.nextInt(3000);
            result = MIN + (rand.nextDouble() * (MAX - MIN));
            result = (double) Math.round(result * count);
            return result;
    }
}

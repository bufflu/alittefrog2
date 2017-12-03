package com.lu.service;

import com.lu.alittefrog.common.RpcContext;

import java.util.Date;

public class SayServiceImpl implements SayService {

    public static int a = 1;

    public String say(String name) {

        RpcContext context = RpcContext.getRpcContext();
        context.setAttachment("服务端返回的附件信息,时间:",new Date());

        //测试异常
        /*int i = 10;
        int j = i/0;
        System.out.println(j);*/
        String str = name + " say Hello!" + a;
        // a++;
        return str;
    }
}

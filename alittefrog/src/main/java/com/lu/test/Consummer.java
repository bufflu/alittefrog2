package com.lu.test;

import com.lu.alittefrog.common.RpcContext;
import com.lu.service.SayService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;
import java.util.Set;

public class Consummer {

    public static void main(String[] args) {
        //ApplicationContext context = new ClassPathXmlApplicationContext("consummer.xml");
        ApplicationContext context = new ClassPathXmlApplicationContext("consummerNew.xml");
        SayService service = (SayService) context.getBean("sayService");
        for(int i=0; i<10; i++) {
            String str = service.say("fwy  fwy");
            System.out.println(str);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /*
        获取附件信息
        Map<Object, Object> allAttachment = RpcContext.getRpcContext().getAllAttachment();
        Set<Object> set = allAttachment.keySet();
        for(Object key: set){
            System.out.println("key:"+key);
            System.out.println("value:"+allAttachment.get(key));
        }*/
    }
}

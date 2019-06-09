package com.yangyang.rpc.demo.client;

import com.yangyang.rpc.core.client.RpcProxy;
import com.yangyang.rpc.demo.api.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by chenshunyang on 2016/11/17.
 */
public class ClientTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-client.xml");

        RpcProxy rpcProxy = context.getBean(RpcProxy.class);
        HelloService helloService = rpcProxy.create(HelloService.class);
        String result = helloService.hello("World");
        System.out.println("result:"+result);

    }
}

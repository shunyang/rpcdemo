package com.yangyang.rpc.test.app;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yangyang.rpc.client.RpcProxy;
import com.yangyang.rpc.test.client.HelloService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-client.xml")
public class HelloServiceTest {
	
	 @Autowired
	 private RpcProxy rpcProxy;

	 @Test
	 public void testHello() {
		 HelloService helloService = rpcProxy.create(HelloService.class);
		  String result = helloService.hello("World");
		  System.out.println("result:"+result);
	      Assert.assertEquals("Hello! World", result);
	 }
}

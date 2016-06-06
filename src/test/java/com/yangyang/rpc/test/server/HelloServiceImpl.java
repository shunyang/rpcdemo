package com.yangyang.rpc.test.server;

import com.yangyang.rpc.server.RpcService;
import com.yangyang.rpc.test.client.HelloService;
import com.yangyang.rpc.test.client.Person;

@RpcService(value = HelloService.class)
public class HelloServiceImpl implements HelloService{

	@Override
	public String hello(String name) {
		 return "Hello! " + name;
	}

	@Override
	public String hello(Person person) {
		return "Hello! " + person.getFirstName() + " " + person.getLastName();
	}

}

package com.yangyang.rpc.demo.server.service;


import com.yangyang.rpc.demo.api.HelloService;
import com.yangyang.rpc.demo.api.bean.Person;
import com.yangyang.rpc.core.server.RpcService;

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

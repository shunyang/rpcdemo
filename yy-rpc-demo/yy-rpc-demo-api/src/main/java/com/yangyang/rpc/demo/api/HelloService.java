package com.yangyang.rpc.demo.api;

import com.yangyang.rpc.demo.api.bean.Person;

public interface HelloService {
	  String hello(String name);

	  String hello(Person person);

}

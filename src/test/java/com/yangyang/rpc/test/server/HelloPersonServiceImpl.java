package com.yangyang.rpc.test.server;

import java.util.ArrayList;
import java.util.List;

import com.yangyang.rpc.server.RpcService;
import com.yangyang.rpc.test.client.HelloPersonService;
import com.yangyang.rpc.test.client.Person;

 //@RpcService(HelloPersonService.class)
public class HelloPersonServiceImpl implements HelloPersonService{

	@Override
	public List<Person> GetTestPerson(String name, int num) {
		  List<Person> persons = new ArrayList<>(num);
	        for (int i = 0; i < num; ++i) {
	            persons.add(new Person(Integer.toString(i), name));
	        }
	        return persons;
	}

}

package com.yangyang.rpc.demo.server.service;

import com.yangyang.rpc.demo.api.HelloPersonService;
import com.yangyang.rpc.demo.api.bean.Person;
import com.yangyang.rpc.core.server.RpcService;

import java.util.ArrayList;
import java.util.List;

@RpcService(HelloPersonService.class)
public class HelloPersonServiceImpl implements HelloPersonService {

   @Override
   public List<Person> GetTestPerson(String name, int num) {
         List<Person> persons = new ArrayList<>(num);
           for (int i = 0; i < num; ++i) {
               persons.add(new Person(Integer.toString(i), name));
           }
           return persons;
   }

}

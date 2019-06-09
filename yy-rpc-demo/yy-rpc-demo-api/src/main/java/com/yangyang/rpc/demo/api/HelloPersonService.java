package com.yangyang.rpc.demo.api;

import com.yangyang.rpc.demo.api.bean.Person;

import java.util.List;

public interface HelloPersonService {
    List<Person> GetTestPerson(String name, int num);

}

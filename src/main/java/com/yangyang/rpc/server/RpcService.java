package com.yangyang.rpc.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;
/**
 * RPC 请求注解（标注在服务实现类上）
 * @author chenshunyang
 *
 */

@Target({ElementType.TYPE}) //在类或者接口上标示
@Retention(RetentionPolicy.RUNTIME)
@Component // 标明可被 Spring 扫描
public @interface RpcService {
	Class<?> value();
}

package com.yangyang.rpc.core.client;

import com.yangyang.rpc.core.registry.ServiceDiscovery;
import com.yangyang.rpc.core.protocol.RpcRequest;
import com.yangyang.rpc.core.protocol.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class RpcProxy {
	
	private String serverAddress;
    private ServiceDiscovery serviceDiscovery;
	public RpcProxy(String serverAddress) {
		super();
		this.serverAddress = serverAddress;
	}
	public RpcProxy(ServiceDiscovery serviceDiscovery) {
		super();
		this.serviceDiscovery = serviceDiscovery;
	}
	
	/**
	 * 得到被代理对象;
	 * chenshunyang
	 * 2016年6月6日 下午9:36:03
	 */
	 @SuppressWarnings("unchecked")
	public <T> T create(Class<?> interfaceClass){
		return (T) Proxy.newProxyInstance(
				  interfaceClass.getClassLoader(),
				  new Class<?>[]{interfaceClass},
				  new InvocationHandler() {
						@Override
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
							RpcRequest request = new RpcRequest();//创建并初始化 RPC 请求
							request.setRequestId(UUID.randomUUID().toString());
							request.setClassName(method.getDeclaringClass().getName());
							request.setMethodName(method.getName());
							request.setParameters(args);
							request.setParameterTypes(method.getParameterTypes());
							
							if (serviceDiscovery != null) {
								serverAddress = serviceDiscovery.discover();// 发现服务
							}
							if (serverAddress == null) {
								throw new RuntimeException("No server address found!Please check your client config");
							}
							String[] array = serverAddress.split(":");
		                    String host = array[0];
		                    int port = Integer.parseInt(array[1]);
		                    RpcClient rpcClient = new RpcClient(host, port); // 初始化 RPC 客户端
		                    RpcResponse response = rpcClient.send(request);
		                    if (response.isError()) {
								throw response.getError();
							}else{
								return response.getResult();
							}
						}
				}
			 );
	 }
}

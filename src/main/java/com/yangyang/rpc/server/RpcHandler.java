package com.yangyang.rpc.server;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yangyang.rpc.protocol.RpcRequest;
import com.yangyang.rpc.protocol.RpcResponse;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

/**
 * RPC服务端:请求处理过程
 * @author chenshunyang
 *
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest>{
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(RpcHandler.class);
	
	private final Map<String, Object> handlerMap;
	
	public RpcHandler(Map<String, Object> handlerMap) {
		this.handlerMap =handlerMap;
	}

	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, RpcRequest request) throws Exception {
		LOGGER.debug("Receive request " + request.toString());
		RpcResponse response = new RpcResponse();
		response.setRequestId(request.getRequestId());
		try {
			Object result = hander(request);
			LOGGER.debug("hander result:"+result);
			response.setResult(result);
		} catch (Throwable t) {
			response.setError(t);
			LOGGER.error("RPC Server handle request error",t);
		}
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private Object hander(RpcRequest request) throws Throwable{
		String className = request.getClassName();
		Object serviceBean = handlerMap.get(className);
		Class<?> serviceClass = serviceBean.getClass();
		String methodName = request.getMethodName();
		Class<?>[] parameterTypes = request.getParameterTypes();
		Object[] parameters = request.getParameters();
		//java反射方式实现
//		Method method = serviceClass.getMethod(methodName, parameterTypes);
//		method.setAccessible(true);
//		return method.invoke(serviceBean, parameters);
		
		//cglib反射方式实现，性能更高
		FastClass serviceFastClass = FastClass.create(serviceClass);
	    FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
	    return serviceFastMethod.invoke(serviceBean, parameters);
	}

	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("server caught exception", cause);
        ctx.close();
    }
}

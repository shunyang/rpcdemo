package com.yangyang.rpc.core.client;

import com.yangyang.rpc.core.protocol.RpcDecoder;
import com.yangyang.rpc.core.protocol.RpcEncoder;
import com.yangyang.rpc.core.protocol.RpcRequest;
import com.yangyang.rpc.core.protocol.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 真正的的客户端
 * @author chenshunyang
 *
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse>{
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);

    private String host;
    private int port;

	
	private RpcResponse response;
	
	private final Object obj = new Object();

	public RpcClient(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
		this.response = response;
		synchronized (obj) {
			obj.notifyAll();
		}
		
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("client caught exception", cause);
        ctx.close();
    }
	
	public RpcResponse send(RpcRequest request) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                            .addLast(new RpcEncoder(RpcRequest.class)) // 将 RPC 请求进行编码（为了发送请求）
                            .addLast(new RpcDecoder(RpcResponse.class)) // 将 RPC 响应进行解码（为了处理响应）
                            .addLast(RpcClient.this); // 使用 RpcClient 发送 RPC 请求
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.connect(host, port).sync();
            //channel.writeAndFlush()方法来发送消息二进制串，这个方法调用后对于整个远程调用(从发出请求到接收到结果)来说是一个异步的
            future.channel().writeAndFlush(request).sync();
            synchronized (obj) {
                obj.wait(); // 未收到响应，使线程等待
            }

            if (response != null) {
                future.channel().closeFuture().sync();
            }
            return response;
        } finally {
            group.shutdownGracefully();
        }
    }

}

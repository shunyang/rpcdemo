package com.yangyang.rpc.protocol;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * RPC解码
 * @author chenshunyang
 *
 */
public class RpcDecoder extends ByteToMessageDecoder{
	
	private Class<?> genericClass;
	
	public RpcDecoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() <4) { //消息编解码时开始4个字节表示消息的长度，也就是消息编码的时候，先写消息的长度，再写消息。
			return;
		}
		in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
        	ctx.close();
		}
        if (in.readableBytes() < dataLength) {
			in.resetReaderIndex();
			return;
		}
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        Object obj = SerializationUtil.deserialize(data,genericClass);
        out.add(obj);
		
	}

}

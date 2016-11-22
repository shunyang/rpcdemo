package com.yangyang.rpc.protocol;

import java.util.HashMap;
import java.util.Map;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * 序列化工具类（基于 Protostuff 实现）
 * @author chenshunyang
 *
 */
public class SerializationUtil {
	
	 private static Map<Class<?>, Schema<?>> cachedSchema = new HashMap<Class<?>, Schema<?>>();
	 private static Objenesis objenesis = new ObjenesisStd(true);

	 /**
	     * 序列化（对象 -> 字节数组）
	     */
	@SuppressWarnings("unchecked")
	public static <T> byte[] serialize(T obj) {
		 Class<T> clazz = (Class<T>) obj.getClass();
		 LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		 try {
            Schema<T> schema = getSchema(clazz);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
		 } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
		 } finally {
            buffer.clear();
		 }
 	 }
	
	/**
     * 反序列化（字节数组 -> 对象）
     */
	public static <T> T deserialize(byte[] data, Class<T> cls) {
		 try {
	            T message = (T) objenesis.newInstance(cls);
	            Schema<T> schema = getSchema(cls);
	            ProtostuffIOUtil.mergeFrom(data, message, schema);
	            return message;
	        } catch (Exception e) {
	            throw new IllegalStateException(e.getMessage(), e);
	        }
	}

	@SuppressWarnings("unchecked")
	private static <T> Schema<T> getSchema(Class<T> clazz) {
		 Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
	     if (schema == null) {
	            schema = RuntimeSchema.createFrom(clazz);
	            if (schema != null) {
	                cachedSchema.put(clazz, schema);
	            }
	     }
	     return schema;
	}

}

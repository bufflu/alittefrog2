package com.lu.alittefrog.serializer;

/**
 * 序列化和反序列化方法
 */
public interface ObjectSerializer {

    public byte[] serializer(Object value);

    public Object deserializer(byte[] bytes);

}

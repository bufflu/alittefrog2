package com.lu.alittefrog.transport;

import com.lu.alittefrog.serializer.JDKSerializer;
import com.lu.alittefrog.serializer.ObjectSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 对象解码器
 */
public class ObjectDecoder extends MessageToMessageDecoder<ByteBuf> {

    private ObjectSerializer serializer = new JDKSerializer();

    public void setSerializer(ObjectSerializer serializer) {
        this.serializer = serializer;
    }

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf,
                          List<Object> list) throws Exception {
        byte[] values = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(values);
        Object object = serializer.deserializer(values);
        list.add(object);
    }
}

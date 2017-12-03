package com.lu.alittefrog.transport;

import com.lu.alittefrog.serializer.JDKSerializer;
import com.lu.alittefrog.serializer.ObjectSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;


public class ObjectEncoder extends MessageToMessageEncoder<Object> {
    private ObjectSerializer serializer = new JDKSerializer();

    public void setSerializer(ObjectSerializer serializer) {
        this.serializer = serializer;
    }

    protected void encode(ChannelHandlerContext ctx, Object msg,
                          List<Object> out) throws Exception {

        byte[] bytes =serializer.serializer(msg);
        ByteBuf buffer= Unpooled.buffer();
        buffer.writeBytes(bytes);
        out.add(buffer);

    }
}

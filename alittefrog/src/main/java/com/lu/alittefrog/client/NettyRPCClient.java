package com.lu.alittefrog.client;

import com.lu.alittefrog.common.HostAndPort;
import com.lu.alittefrog.protocol.MethodInvokeMetaWrap;
import com.lu.alittefrog.protocol.ResultWrap;
import com.lu.alittefrog.transport.ObjectDecoder;
import com.lu.alittefrog.transport.ObjectEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class NettyRPCClient implements RPCClient {
    private Bootstrap bt;
    private EventLoopGroup worker;

    //初始化方法
    public void init() {
        bt = new Bootstrap();
        worker = new NioEventLoopGroup();
        bt.group(worker);
        bt.channel(NioSocketChannel.class);
    }

    //执行
    public ResultWrap invoke(final MethodInvokeMetaWrap mimw, HostAndPort hostAndPort) {
        bt.handler(new ChannelInitializer<SocketChannel>() {

            protected void initChannel(SocketChannel sc) throws Exception {
                ChannelPipeline pipeline = sc.pipeline();
                //数据帧解码
                pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
                //解码对象
                pipeline.addLast(new ObjectDecoder());
                //数据帧编码
                pipeline.addLast(new LengthFieldPrepender(2));
                //编码对象
                pipeline.addLast(new ObjectEncoder());
                //添加最终处理类
                pipeline.addLast(new ChannelHandlerAdapter(){

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        System.err.println(cause);
                        cause.printStackTrace();
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ResultWrap resultWrap = (ResultWrap) msg;
                        mimw.setResultWrap(resultWrap);
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        ChannelFuture channelFuture = ctx.writeAndFlush(mimw);
                        channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                    }
                });
            }
        });

        try {
            ChannelFuture channelFuture = bt.connect(hostAndPort.getHost(), hostAndPort.getPort());
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mimw.getResultWrap();
    }

    //关闭方法
    public void close() {
        worker.shutdownGracefully();
    }
}

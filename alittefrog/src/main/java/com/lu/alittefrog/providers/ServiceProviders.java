package com.lu.alittefrog.providers;

import com.lu.alittefrog.common.HostAndPort;
import com.lu.alittefrog.common.RpcContext;
import com.lu.alittefrog.protocol.MethodInvokeMeta;
import com.lu.alittefrog.protocol.MethodInvokeMetaWrap;
import com.lu.alittefrog.protocol.Result;
import com.lu.alittefrog.protocol.ResultWrap;
import com.lu.alittefrog.registry.Registry;
import com.lu.alittefrog.transport.ObjectDecoder;
import com.lu.alittefrog.transport.ObjectEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class ServiceProviders {

    //bean工厂并提供set方法
    private Map<Class,Object> exposeBeanMap;

    public void setExposeBeanMap(Map<Class, Object> exposeBeanMap) {
        this.exposeBeanMap = exposeBeanMap;
    }

    //服务器相关
    private ServerBootstrap serverBootstrap;
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private int port;

    //注册中心
    private Registry registry;

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    //构造方法
    public ServiceProviders(int port) {
        this.port = port;
    }

    //初始化方法
    public void init() throws UnknownHostException {
        serverBootstrap = new ServerBootstrap();
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        serverBootstrap.group(boss,worker);
        serverBootstrap.channel(NioServerSocketChannel.class);

        start();
    }

    //start方法
    public void start() throws UnknownHostException {
        //初始化通道
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
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
                //最终处理者
                pipeline.addLast(new ChannelHandlerAdapter(){
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        System.out.println(">>>有异常了>>>"+cause);
                        cause.printStackTrace();
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                        MethodInvokeMetaWrap mimw = (MethodInvokeMetaWrap) msg;
                        MethodInvokeMeta invokeMeta = mimw.getInvokeMeta();
                        //附件
                        Map<Object, Object> attchments = mimw.getAttchment();
                        //将附件信息放入ThreadLocal中
                        RpcContext.getRpcContext().getAllAttachment().putAll(attchments);

                        Class targetClass = invokeMeta.getTargetClass();
                        String methodName = invokeMeta.getMethodName();
                        Class<?>[] parameterTypes = invokeMeta.getParameterTypes();

                        //调用本地工厂
                        Object target = exposeBeanMap.get(targetClass);
                        Method method = target.getClass().getDeclaredMethod(methodName, parameterTypes);
                        //判断方法是否私有
                        if (method.isAccessible()){
                            method.setAccessible(true);
                        }

                        ResultWrap resultWrap = new ResultWrap();
                        Result result = new Result();
                        try {
                            Object object = method.invoke(target, invokeMeta.getArgs());
                            result.setResult(object);
                            //放置附件信息
                            resultWrap.setAttchment(RpcContext.getRpcContext().getAllAttachment());

                        }catch (Exception e){
                            result.setException(new RuntimeException(e.getCause()));
                        }
                        resultWrap.setResult(result);

                        ChannelFuture channelFuture = ctx.writeAndFlush(resultWrap);
                        channelFuture.addListener(ChannelFutureListener.CLOSE);
                        channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                    }
                });
            }
        });

        //注册服务
        String host = InetAddress.getLocalHost().getHostAddress();
        for(Class target : exposeBeanMap.keySet()){
            registry.register(target,new HostAndPort(host,port));
        }

        new Thread(){
            @Override
            public void run() {
                //绑定端并且监听服务
                try {
                    System.out.println(">>>监听端口>>>"+port);
                    ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
                    channelFuture.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void  close(){
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }
}

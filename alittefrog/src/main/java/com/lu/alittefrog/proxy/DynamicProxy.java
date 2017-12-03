package com.lu.alittefrog.proxy;

import com.lu.alittefrog.common.HostAndPort;
import com.lu.alittefrog.common.RpcContext;
import com.lu.alittefrog.failover.Cluster;
import com.lu.alittefrog.protocol.MethodInvokeMeta;
import com.lu.alittefrog.protocol.MethodInvokeMetaWrap;
import com.lu.alittefrog.protocol.Result;
import com.lu.alittefrog.protocol.ResultWrap;
import com.lu.alittefrog.registry.Registry;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * 动态代理对象
 */
public class DynamicProxy implements InvocationHandler,FactoryBean {

    private Class targetInterface;
    private List<HostAndPort> hostAndPortList;//服务当前列表
    private Registry registry;
    private Cluster cluster;

    //构造方法
    public DynamicProxy(Class targetInterface, Registry registry, Cluster cluster) {
        this.targetInterface = targetInterface;
        this.registry = registry;
        this.cluster = cluster;

        //查询服务列表
        hostAndPortList = registry.retriveService(targetInterface);
        //订阅服务变更
        registry.subscrible(targetInterface, hostAndPortList);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodInvokeMeta mim = new MethodInvokeMeta(targetInterface,method.getName(),
                method.getParameterTypes(),args);
        MethodInvokeMetaWrap mimw = new MethodInvokeMetaWrap();
        mimw.setInvokeMeta(mim);

        //设置附件信息
        Map<Object, Object> attachments = RpcContext.getRpcContext().getAllAttachment();
        mimw.setAttchment(attachments);

        //发送网络请求
        ResultWrap resultWrap = cluster.invoke(hostAndPortList, mimw);

        Result result = resultWrap.getResult();
        if(result.getException()!=null){
            throw result.getException();
        }
        //客户端的附件和服务端的附件合并
        attachments.putAll(resultWrap.getAttchment());
        return result.getResult();
    }

    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(DynamicProxy.class.getClassLoader(),
                new Class[]{targetInterface},this);
    }

    public Class<?> getObjectType() {
        return targetInterface;
    }

    public boolean isSingleton() {
        return true;
    }
}

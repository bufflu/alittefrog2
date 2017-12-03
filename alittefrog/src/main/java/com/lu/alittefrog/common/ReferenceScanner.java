package com.lu.alittefrog.common;

import com.lu.alittefrog.client.NettyRPCClient;
import com.lu.alittefrog.failover.FailFastCluster;
import com.lu.alittefrog.loadbalance.RandomLoadBalancer;
import com.lu.alittefrog.loadbalance.RoundRobinLoadBalancer;
import com.lu.alittefrog.proxy.DynamicProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Map;
import java.util.Set;

public class ReferenceScanner implements BeanFactoryPostProcessor {

    private Map<String, Class> references;
    private String registryName;
    private String loadBalance = "random";

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }

    public void setReferences(Map<String, Class> references) {
        this.references = references;
    }

    public void setRegistryName(String registryName) {
        this.registryName = registryName;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        //用来注册
        DefaultListableBeanFactory DefaultBF = (DefaultListableBeanFactory) beanFactory;


//      <!--客户端-->
//      <bean class="com.lu.alittefrog.client.NettyRPCClient" id="client" init-method="init" destroy-method="close"></bean>
        BeanDefinitionBuilder client = BeanDefinitionBuilder.genericBeanDefinition(NettyRPCClient.class);
        client.setInitMethodName("init");
        client.setDestroyMethodName("close");
        DefaultBF.registerBeanDefinition("client",client.getBeanDefinition());


//      <bean class="com.lu.alittefrog.loadbalance.RandomLoadBalancer" id="loadBalancer"></bean>
        BeanDefinitionBuilder loadBalancer = BeanDefinitionBuilder.genericBeanDefinition(RandomLoadBalancer.class);
        System.out.println("loadBalance-----"+loadBalance);
        if(loadBalance.equals("roundRobin")){
            loadBalancer = BeanDefinitionBuilder.genericBeanDefinition(RoundRobinLoadBalancer.class);
        }
        DefaultBF.registerBeanDefinition("loadBalancer",loadBalancer.getBeanDefinition());


//      <!--构建Cluster-->
//      <bean class="com.lu.alittefrog.failover.FailFastCluster" id="cluster">
//            <constructor-arg name="rpcClient" ref="client"/>
//            <constructor-arg name="loadBalancer" ref="loadBalancer"/>
//      </bean>
        BeanDefinitionBuilder cluster = BeanDefinitionBuilder.genericBeanDefinition(FailFastCluster.class);
        cluster.addConstructorArgReference("client");
        cluster.addConstructorArgReference("loadBalancer");
        DefaultBF.registerBeanDefinition("cluster",cluster.getBeanDefinition());


//      <!--代理对象-->
//      <bean class="com.lu.alittefrog.proxy.DynamicProxy" id="sayService">
//            <constructor-arg name="cluster" ref="cluster"/>
//            <constructor-arg name="registry" ref="registry"/>
//            <constructor-arg name="targetInterface" value="com.lu.service.SayService"/>
//      </bean>
        Set<Map.Entry<String, Class>> entries = references.entrySet();
        for (Map.Entry<String, Class> entry : entries) {
            String beanId = entry.getKey();
            Class targetClass = entry.getValue();

            BeanDefinitionBuilder proxy = BeanDefinitionBuilder.genericBeanDefinition(DynamicProxy.class);
            proxy.addConstructorArgValue(targetClass);
            proxy.addConstructorArgReference(registryName);
            proxy.addConstructorArgReference("cluster");
            DefaultBF.registerBeanDefinition(beanId, proxy.getBeanDefinition());
        }

    }
}

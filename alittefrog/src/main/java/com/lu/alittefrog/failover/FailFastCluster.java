package com.lu.alittefrog.failover;

import com.lu.alittefrog.client.RPCClient;
import com.lu.alittefrog.common.HostAndPort;
import com.lu.alittefrog.loadbalance.LoadBalancer;
import com.lu.alittefrog.protocol.MethodInvokeMetaWrap;
import com.lu.alittefrog.protocol.ResultWrap;

import java.util.List;

public class FailFastCluster implements Cluster {

    //客户端
    private RPCClient rpcClient;
    //负载均衡策略
    private LoadBalancer loadBalancer;

    public FailFastCluster(RPCClient rpcClient, LoadBalancer loadBalancer) {
        this.rpcClient = rpcClient;
        this.loadBalancer = loadBalancer;
    }

    public ResultWrap invoke(List<HostAndPort> hostAndPortList, MethodInvokeMetaWrap mimw) {
        HostAndPort select = loadBalancer.select(hostAndPortList, mimw);
        ResultWrap resultWrap = rpcClient.invoke(mimw, select);
        return resultWrap;
    }
}

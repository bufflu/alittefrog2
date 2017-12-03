package com.lu.alittefrog.loadbalance;

import com.lu.alittefrog.common.HostAndPort;
import com.lu.alittefrog.protocol.MethodInvokeMetaWrap;

import java.util.List;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {
    public HostAndPort select(List<HostAndPort> hostAndPortList, MethodInvokeMetaWrap mimw) {
        int i = new Random().nextInt(hostAndPortList.size());
        return hostAndPortList.get(i);
    }
}

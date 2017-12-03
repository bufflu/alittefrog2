package com.lu.alittefrog.loadbalance;

import com.lu.alittefrog.common.HostAndPort;
import com.lu.alittefrog.protocol.MethodInvokeMetaWrap;

import java.util.List;

public class RoundRobinLoadBalancer implements LoadBalancer {

    private int round = 0;
    public HostAndPort select(List<HostAndPort> hostAndPortList, MethodInvokeMetaWrap mimw) {
        int n = hostAndPortList.size();
        int i = round % n;
        round ++;
        if(round < 0){
            round = 0;
        }
        return hostAndPortList.get(i);
    }
}

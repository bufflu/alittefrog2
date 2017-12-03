package com.lu.alittefrog.loadbalance;

import com.lu.alittefrog.common.HostAndPort;
import com.lu.alittefrog.protocol.MethodInvokeMetaWrap;

import java.util.List;

public interface LoadBalancer {
    HostAndPort select(List<HostAndPort> hostAndPortList, MethodInvokeMetaWrap mimw);
}

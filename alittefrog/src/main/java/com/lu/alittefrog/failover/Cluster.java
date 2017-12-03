package com.lu.alittefrog.failover;

import com.lu.alittefrog.common.HostAndPort;
import com.lu.alittefrog.protocol.MethodInvokeMetaWrap;
import com.lu.alittefrog.protocol.ResultWrap;

import java.util.List;

public interface Cluster {

    ResultWrap invoke(List<HostAndPort> hostAndPortList, MethodInvokeMetaWrap mimw);
}

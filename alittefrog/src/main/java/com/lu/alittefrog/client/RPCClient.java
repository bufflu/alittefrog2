package com.lu.alittefrog.client;

import com.lu.alittefrog.common.HostAndPort;
import com.lu.alittefrog.protocol.MethodInvokeMetaWrap;
import com.lu.alittefrog.protocol.ResultWrap;

public interface RPCClient {
    void init();
    ResultWrap invoke(MethodInvokeMetaWrap mimw, HostAndPort hostAndPort);
    void close();
}

package com.lu.alittefrog.registry;

import com.lu.alittefrog.common.HostAndPort;

import java.util.List;

/**
 * 注册接口
 */
public interface Registry {

    String PREFIX = "/RPC";

    String SUFFIX = "/providers";

    void register(Class targetService, HostAndPort hostAndPort);

    void subscrible(Class targetService, List<HostAndPort> hostAndPortList);

    List<HostAndPort> retriveService(Class targerService);

    void close();
}

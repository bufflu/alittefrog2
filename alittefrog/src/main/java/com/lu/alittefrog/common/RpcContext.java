package com.lu.alittefrog.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例的
 */
public class RpcContext {

    private static final ThreadLocal<RpcContext> RPC_CONTEXT_THREAD_LOCAL = new InheritableThreadLocal<RpcContext>();

    private RpcContext() {
    }

    private Map<Object, Object> attachments = new HashMap<Object, Object>();

    public static synchronized RpcContext getRpcContext(){
        RpcContext rpcContext = RPC_CONTEXT_THREAD_LOCAL.get();
        if (rpcContext != null){
            return rpcContext;
        }
        RPC_CONTEXT_THREAD_LOCAL.set(new RpcContext());
        return RPC_CONTEXT_THREAD_LOCAL.get();
    }

    public Map<Object, Object> getAllAttachment() {
        return attachments;
    }

    public void setAttachment (Object key, Object value){
        attachments.put(key, value);
    }

    public Object getAttachment (Object key){
        return attachments.get(key);
    }
}

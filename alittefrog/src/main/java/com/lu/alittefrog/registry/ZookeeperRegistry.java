package com.lu.alittefrog.registry;

import com.lu.alittefrog.common.HostAndPort;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.Vector;

public class ZookeeperRegistry implements Registry {

    private ZkClient zkClient;

    //构造方法
    public ZookeeperRegistry(String serers){
        this.zkClient = new ZkClient(serers);
    }

    //注册服务
    public void register(Class targetService, HostAndPort hostAndPort) {
        String node = PREFIX + "/" + targetService.getName() + SUFFIX;
        if (!zkClient.exists(node)){
            zkClient.createPersistent(node,true);
        }
        String tmpNode = node + "/" + hostAndPort.getHost() + ":" + hostAndPort.getPort();
        if(!zkClient.exists(tmpNode)){
            zkClient.deleteRecursive(tmpNode);
        }
        zkClient.createEphemeral(tmpNode);
    }

    //跟新
    public void subscrible(Class targetService, final List<HostAndPort> hostAndPortList) {
        String node = PREFIX + "/" + targetService.getName() + SUFFIX;
        zkClient.subscribeChildChanges(node, new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> chidNodes) throws Exception {
                System.out.println(">>>>>>>>subscrible>>>>>>");
                hostAndPortList.clear();
                for(String chidNode: chidNodes){
                    HostAndPort hostAndPort = new HostAndPort();
                    hostAndPort.setHost(chidNode.split(":")[0]);
                    hostAndPort.setPort(Integer.parseInt(chidNode.split(":")[1]));
                    hostAndPortList.add(hostAndPort);
                }
            }
        });
    }

    //提供
    public List<HostAndPort> retriveService(Class targetService) {
        List<HostAndPort> hostAndPortList = new Vector<HostAndPort>();
        String node = PREFIX + "/" + targetService.getName() + SUFFIX;
        List<String> childrens = zkClient.getChildren(node);
        for (String childNode: childrens) {
            HostAndPort hostAndPort = new HostAndPort();
            hostAndPort.setHost(childNode.split(":")[0]);
            hostAndPort.setPort(Integer.parseInt(childNode.split(":")[1]));
            hostAndPortList.add(hostAndPort);
        }
        return hostAndPortList;
    }

    //关闭
    public void close() {
        zkClient.close();
    }
}

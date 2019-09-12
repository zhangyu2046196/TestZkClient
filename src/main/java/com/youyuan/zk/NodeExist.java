package com.youyuan.zk;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * @author zhangyu
 * @description zk开源客户端 判断节点是否存在
 * @date 2018/9/10 22:08
 */
public class NodeExist {
    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",10000,10000,new SerializableSerializer());
        boolean isExist=zkClient.exists("/myconfig/driver");
        System.out.println("判断节点是否存在:"+isExist);
    }
}

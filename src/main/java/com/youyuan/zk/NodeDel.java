package com.youyuan.zk;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * @author zhangyu
 * @description zk开源客户端 ZkClient删除节点
 * @date 2018/9/10 22:19
 */
public class NodeDel {
    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",10000,10000,new SerializableSerializer());
        boolean isDel=zkClient.delete("/myconfig/url");
        System.out.println("删除节点结果:"+isDel);
        if (zkClient!=null){
            zkClient.close();
        }
    }
}

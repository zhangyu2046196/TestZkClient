package com.youyuan.zk;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.data.Stat;

/**
 * @author zhangyu
 * @description zk开源客户端ZkClient查询节点内容
 * @date 2018/9/10 21:54
 */
public class GetData {
    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",10000,10000,new SerializableSerializer());
        Object result=zkClient.readData("/myconfig/username",new Stat());
        System.out.println("节点内容值:"+result);
    }
}

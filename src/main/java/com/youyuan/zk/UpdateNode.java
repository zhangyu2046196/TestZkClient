package com.youyuan.zk;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * @author zhangyu
 * @description zk开源客户端ZkClient更新节点
 * @date 2018/9/10 21:35
 */
public class UpdateNode {
    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",10000,10000,new SerializableSerializer());
        zkClient.writeData("/myconfig/user","有钱，健康");
        if (zkClient!=null){
            zkClient.close();
        }
    }
}

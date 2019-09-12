package com.youyuan.zk;

import com.youyuan.zk.bean.User;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;

/**
 * @author zhangyu
 * @description 递归创建子节点
 * @date 2018/9/11 15:34
 */
public class CreateChildrenNode {
    private static String host="172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181";

    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient(host,10000,10000,new SerializableSerializer());
        zkClient.createPersistent("/myconfig/A/A_1",true);//递归创建节点
        if (zkClient!=null){
            zkClient.close();
        }
    }
}

package com.youyuan.zk;

import com.youyuan.zk.bean.User;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;

/**
 * @author zhangyu
 * @description 节点保存对象
 * @date 2018/9/11 13:34
 */
public class CreateNodeObject {
    private static String host="172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181";

    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient(host,10000,10000,new SerializableSerializer());
        zkClient.create("/myconfig/userObject",new User("张三",20), CreateMode.PERSISTENT);
        if (zkClient!=null){
            zkClient.close();
        }
    }
}

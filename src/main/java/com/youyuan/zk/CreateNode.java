package com.youyuan.zk;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;

/**
 * @author zhangyu
 * @description zk开源客户端 ZkClient创建节点
 * @date 2018/9/10 20:54
 */
public class CreateNode {
    public static void main(String[] args) {
        //第一个参数服务器ip
        //第二个参数session超时时间 单位毫秒
        //第三个参数服务超时时间 单位毫秒
        //第四个参数序列化器,用于存储对象
        //new SerializableSerializer()  针对非对象序列表   BytesPushThroughSerializer 针对对象序列化，实际就是转为字节数组
        ZkClient zkClient=new ZkClient("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",10000,10000,new SerializableSerializer());
        String result=zkClient.create("/myconfig/user","富强", CreateMode.PERSISTENT);
        zkClient.create("/myconfig/username","root",CreateMode.PERSISTENT);
        zkClient.create("/myconfig/password","123456",CreateMode.PERSISTENT);
        zkClient.create("/myconfig/url","http://youyuan.com",CreateMode.PERSISTENT);
        zkClient.create("/myconfig/driver","驱动",CreateMode.PERSISTENT);
        System.out.println("创建节点返回结果:"+result);
        if (zkClient!=null){
            zkClient.close();
        }
    }
}

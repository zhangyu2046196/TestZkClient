package com.youyuan.zk;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;

/**
 * zookeeper开源客户端框架zkClient
 * @author zhangyu
 * @date 2018/9/6 22:56
 */
public class CreateSession {
    public static void main(String[] args) {
        //第一个参数zookeeper服务端地址
        //第二个参数会话超时时间 单位 毫秒
        //第三个参数连接超时时间 单位 毫秒
        //第四个参数序列化器对象，因为节点存储的数据可能为实体对象
        ZkClient zkClient=new ZkClient("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",10000,10000,new SerializableSerializer());//创建一个客户端连接服务端的会话  session
        System.out.println("客户端连接服务端状态:成功");//客户端连接服务端状态
        //zkClient.create("/myconfig/ip","19792", CreateMode.PERSISTENT);
        zkClient.delete("/myconfig/ip");
        zkClient.close();
    }
}

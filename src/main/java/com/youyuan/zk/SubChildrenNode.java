package com.youyuan.zk;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * @author zhangyu
 * @description zk开源客户端ZkClient获取子节点列表
 * @date 2018/9/10 21:42
 */
public class SubChildrenNode {
    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",10000,10000,new SerializableSerializer());
        List<String> childrens= zkClient.getChildren("/myconfig");
        for (String children:childrens){
            System.out.println("子节点名称:"+children+" 子节点内容:"+zkClient.readData("/myconfig/"+children,new Stat()));
        }
    }
}

package com.youyuan.zk;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * @author zhangyu
 * @description 递归删除节点
 * @date 2018/9/11 15:43
 */
public class DeleteChildrenNode {
    private static String host="172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181";

    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient(host,10000,10000,new SerializableSerializer());
        boolean isDel=zkClient.deleteRecursive("/myconfig/A");
        System.out.println("zk递归删除节点结果:"+isDel);
        if (zkClient!=null){
            zkClient.close();
        }
    }
}

package com.youyuan.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.data.Stat;

/**
 * @author zhangyu
 * @description 重复注册watche监听节点内容
 * @date 2018/9/11 21:08
 */
public class SubcrbDataChangeNode {
    private static String host="172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181";

    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient(host,10000,10000,new SerializableSerializer());
        zkClient.subscribeDataChanges("/youyuan", new IZkDataListener() {//监听节点内容发生变化
            //监听节点路径和节点内容是否发生变化
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("监听节点内容路径:"+dataPath+" 节点内容信息:"+data);
            }
            //删除的节点
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("删除节点路径:"+dataPath);
            }
        });
        Object date=zkClient.readData("/youyuan",new Stat());
        System.out.println("节点内容信息:"+date);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

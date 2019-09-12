package com.youyuan.master;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyu
 * @version 1.0
 * @description 测试通过zk的临时节点特性实现的master选主代码
 * @date 2018/9/12 21:18
 */
public class SelectorLeaderZkClient {
    //创建应用个数
    private static Integer QUERY_NUM=10;
    //zk集群地址
    private static String hosts="172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181";
    //存储zkClient集合
    private static List<ZkClient> zkClientList=new ArrayList<ZkClient>();
    //存储WorkServer
    private static List<WorkServer> workServerList=new ArrayList<WorkServer>();

    public static void main(String[] args) throws Exception {
        try {
            for (int i=0;i<10;i++){
                ZkClient zkClient=new ZkClient(hosts,10000,10000,new SerializableSerializer());
                zkClientList.add(zkClient);
                WorkServer workServer=new WorkServer(new RunningData("client "+i,i));
                workServer.setZkClient(zkClient);
                workServerList.add(workServer);
                workServer.start();
            }
        }finally {
            for(WorkServer workServer:workServerList){
                workServer.stop();
            }
            for (ZkClient zkClient:zkClientList){
                zkClient.close();
            }
        }
    }
}

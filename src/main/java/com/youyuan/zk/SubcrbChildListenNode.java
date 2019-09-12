package com.youyuan.zk;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * @author zhangyu
 * @description 监听指定节点的变化
 * @date 2018/9/11 17:30
 */
public class SubcrbChildListenNode {
    private static String host="172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181";

    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient(host,10000,10000,new SerializableSerializer());
        zkClient.subscribeChildChanges("/youyuan", new IZkChildListener() {//监听指定节点的变化,此接口不会监听节点内容的变化
            //第一个参数节点路径
            //第二个参数子节点列表
            //此监听不能递归监听
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("监听节点路径:"+parentPath+" 子节点列表:"+currentChilds);
            }
        });

//        //新增主节点
//        zkClient.create("/youyuan","有缘", CreateMode.PERSISTENT);
//        //新增子节点youyuan_1
//        zkClient.create("/youyuan/youyuan_1","有缘1",CreateMode.PERSISTENT);
//        //新增子节点youyuan_2
//        zkClient.create("/youyuan/youyuan_2","有缘2",CreateMode.PERSISTENT);
//        //递归创建子节点youyuan_3/youyuan_3_1
//        zkClient.createPersistent("/youyuan/youyuan_3/youyuan_3_1",true);
//        //删除子节点
//        zkClient.delete("/youyuan/youyuan_2");
//        //递归删除youyuan节点
//        zkClient.deleteRecursive("/youyuan");

          List<String> childrens=zkClient.getChildren("/youyuan");
          for (String children:childrens){
              System.out.println("子节点列表信息:"+children);
          }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

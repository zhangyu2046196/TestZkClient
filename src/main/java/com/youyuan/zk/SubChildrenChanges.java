package com.youyuan.zk;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.List;

/**
 * @author zhangyu
 * @description zk开源客户端端 ZkClient重复注册
 * @date 2018/9/10 22:32
 */
public class SubChildrenChanges {
    private static ZkClient zkClient=new ZkClient("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",10000,10000,new SerializableSerializer());

    static class MyIZkChildListener implements IZkChildListener{

        public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
            System.out.println("监听路径:"+parentPath+" children:"+currentChilds);
        }
    }

    public static void main(String[] args) {
      List<String> childrens= zkClient.subscribeChildChanges("/myconfig/user",new MyIZkChildListener());
      for (String children:childrens){
          System.out.println("获取内容:"+children);
      }
    }
}

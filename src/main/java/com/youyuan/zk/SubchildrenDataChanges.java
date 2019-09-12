package com.youyuan.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

/**
 * @author zhangyu
 * @description
 * @date 2018/9/10 22:40
 */
public class SubchildrenDataChanges {
    private static class ZkDataListener implements IZkDataListener {

        public void handleDataChange(String dataPath, Object data)
                throws Exception {
            // TODO Auto-generated method stub
            System.out.println(dataPath+":"+data.toString());
        }

        public void handleDataDeleted(String dataPath) throws Exception {
            // TODO Auto-generated method stub
            System.out.println(dataPath);

        }




    }

    public static void main(String[] args) throws InterruptedException {
        ZkClient zc = new ZkClient("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",10000,10000,new BytesPushThroughSerializer());
        System.out.println("conneted ok!");

        zc.subscribeDataChanges("/myconfig/user", new ZkDataListener());
        Thread.sleep(Integer.MAX_VALUE);


    }
}

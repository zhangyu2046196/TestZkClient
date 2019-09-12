package com.youyuan.zk;

import com.youyuan.zk.bean.User;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.data.Stat;

/**
 * @author zhangyu
 * @description
 * @date 2018/9/11 13:43
 */
public class UpdateNodeObject {
    private static String host="172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181";

    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient(host,10000,10000,new SerializableSerializer());
        zkClient.writeData("/myconfig/userObject",new User("支付中心系统",19));
        User user=zkClient.readData("/myconfig/userObject",new Stat());
        System.out.println("查询节点内容:"+user.toString());
        if (zkClient!=null){
            zkClient.close();
        }
    }
}

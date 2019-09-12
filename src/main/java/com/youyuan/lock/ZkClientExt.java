package com.youyuan.lock;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.Callable;

/**
 * @author zhangyu
 * @version 1.0
 * @description
 * @date 2018/9/17 22:01
 */
public class ZkClientExt extends ZkClient {

    public ZkClientExt(String zkServers, int sessionTimeout, int connectionTimeout, ZkSerializer zkSerializer) {
        super(zkServers, sessionTimeout, connectionTimeout, zkSerializer);
    }

    @Override
    public void watchForData(final String path) {
        retryUntilConnected(new Callable<Object>() {

            public Object call() throws Exception {
                Stat stat = new Stat();
                _connection.readData(path, stat, true);
                return null;
            }

        });
    }

}

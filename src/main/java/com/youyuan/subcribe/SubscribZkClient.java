package com.youyuan.subcribe;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyu 调度器
 * @version 1.0
 * @description
 * @date 2018/9/16 20:42
 */
public class SubscribZkClient {
    private static final int  CLIENT_QTY = 5; // Work Server数量

    private static final String  ZOOKEEPER_SERVER = "172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181";

    private static final String  CONFIG_PATH = "/config";
    private static final String  COMMAND_PATH = "/command";
    private static final String  SERVERS_PATH = "/servers";

    public static void main(String[] args) throws Exception {

        List<ZkClient> clients = new ArrayList<ZkClient>();
        List<WorkServer>  workServers = new ArrayList<WorkServer>();
        ManageServer manageServer = null;

        try {

            // 创建一个默认的配置
            ServerConfig serverConfig = new ServerConfig("root", "123456", "jdbc:mysql://localhost:3306/mydb");

            // 实例化一个Manage Server
            ZkClient clientManage = new ZkClient(ZOOKEEPER_SERVER, 5000, 5000, new BytesPushThroughSerializer());
            manageServer = new ManageServer(SERVERS_PATH, COMMAND_PATH,CONFIG_PATH,clientManage,serverConfig);
            manageServer.start(); // 启动Manage Server

            // 创建指定个数的工作服务器
            for ( int i = 0; i < CLIENT_QTY; ++i ) {
                ZkClient client = new ZkClient(ZOOKEEPER_SERVER, 5000, 5000, new BytesPushThroughSerializer());
                clients.add(client);
                ServerData serverData = new ServerData("192.168.1."+i, i, "WorkServer"+i);

                WorkServer  workServer = new WorkServer(client, CONFIG_PATH, SERVERS_PATH, serverConfig, serverData);
                workServers.add(workServer);
                workServer.start();    // 启动工作服务器

            }

            System.out.println("敲回车键退出！\n");
            new BufferedReader(new InputStreamReader(System.in)).readLine();

        } finally {
            System.out.println("Shutting down...");

            for ( WorkServer workServer : workServers ) {
                try {
                    workServer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for ( ZkClient client : clients ) {
                try {
                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

}

package com.youyuan.subcribe;

import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.util.List;

/**
 * @author zhangyu
 * @version 1.0
 * @description zk应用场景发布订阅 管理服务器
 * @date 2018/9/16 20:16
 */
public class ManageServer {
    //配置节点路径
    private String configPath;
    //服务节点路径
    private String serverPath;
    //command节点路径
    private String commandPath;
    //serverPath节点的监听器
    private IZkChildListener iZkChildListener;
    //command节点数据变化监听器
    private IZkDataListener iZkDataListener;
    //ZkClient客户端
    private ZkClient zkClient;
    //工作服务器列表
    private List<String> workServerList;
    //配置信息实体
    private ServerConfig serverConfig;

    public ManageServer(String configPath, String serverPath, String commandPath, ZkClient zkClient, ServerConfig serverConfig) {
        this.configPath = configPath;
        this.serverPath = serverPath;
        this.commandPath = commandPath;
        this.zkClient = zkClient;
        this.serverConfig = serverConfig;
        this.iZkChildListener=new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("监听serverPath节点发生变化,监听节点信息:"+parentPath+" 节点列表信息:"+currentChilds);
                workServerList=currentChilds;
                execList();
            }
        };
        this.iZkDataListener=new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                String cmd = new String((byte[]) data);
                System.out.println("cmd:"+cmd);
                exeCmd(cmd); // 执行命令
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {

            }
        };
    }

    /*
     * @Description 启动工作服务器
     * @Author zhangyu
     * @Date 2018/9/16 20:39
     * @Param []
     * @return void
     **/
    public void start() {
        initRunning();
    }

    /*
     * @Description
     * @Author zhangyu
     * @Date 2018/9/16 20:39
     * @Param []
     * @return void
     **/
    private void initRunning() {
        zkClient.subscribeDataChanges(commandPath, iZkDataListener);
        zkClient.subscribeChildChanges(serverPath, iZkChildListener);
    }

    /*
     * @Description 停止工作服务器
     * @Author zhangyu
     * @Date 2018/9/16 20:39
     * @Param []
     * @return void
     **/
    public void stop() {
        zkClient.unsubscribeChildChanges(serverPath, iZkChildListener);
        zkClient.unsubscribeDataChanges(commandPath, iZkDataListener);
    }

    /*
     * @Description 执行命令   1: list 2: create 3: modify
     * @Author zhangyu
     * @Date 2018/9/16 20:32
     * @Param [cmd]
     * @return void
     **/
    private void exeCmd(String cmd) {
        if ("list".equals(cmd)) {
            execList();

        } else if ("create".equals(cmd)) {
            execCreate();
        } else if ("modify".equals(cmd)) {
            execModify();
        } else {
            System.out.println("error command!" + cmd);
        }
    }

    /*
     * @Description  修改serverConfig节点内容
     * @Author zhangyu
     * @Date 2018/9/16 20:35
     * @Param []
     * @return void
     **/
    private void execModify() {
        // 我们随意修改config的一个属性就可以了
        serverConfig.setUserName(serverConfig.getUserName() + "_modify");

        try {
            zkClient.writeData(configPath, JSON.toJSONString(serverConfig).getBytes());
        } catch (ZkNoNodeException e) {
            execCreate(); // 写入时config节点还未存在，则创建它
        }
    }

    /*
     * @Description 创建config节点
     * @Author zhangyu
     * @Date 2018/9/16 20:34
     * @Param []
     * @return void
     **/
    private void execCreate() {
        if (!zkClient.exists(configPath)) {
            try {
                zkClient.createPersistent(configPath, JSON.toJSONString(serverConfig)
                        .getBytes());
            } catch (ZkNodeExistsException e) {
                zkClient.writeData(configPath, JSON.toJSONString(serverConfig)
                        .getBytes()); // config节点已经存在，则写入内容就可以了
            } catch (ZkNoNodeException e) {
                String parentDir = configPath.substring(0,
                        configPath.lastIndexOf('/'));
                zkClient.createPersistent(parentDir, true);
                execCreate();
            }
        }
    }

    /*
     * @Description 列出工作列表信息
     * @Author zhangyu
     * @Date 2018/9/16 20:30
     * @Param []
     * @return void
     **/
    private void execList() {
        System.out.println(workServerList.toString());
    }
}

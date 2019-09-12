package com.youyuan.subcribe;

import com.alibaba.fastjson.JSONObject;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

/**
 * @author zhangyu
 * @version 1.0
 * @description zk应用场景发布订阅  此实体代表应用服务器
 * @date 2018/9/13 23:10
 */
public class WorkServer {
    //zk开源客户端框架zkClient
    private ZkClient zkClient;
    //配置节点路径
    private String configPath;
    //server节点路径
    private String serverPath;
    //配置信息实体
    private ServerConfig serverConfig;
    //应用服务器本机实体信息
    private ServerData serverData;
    //zk监听器
    private IZkDataListener iZkDataListener;

    public WorkServer(ZkClient zkClient, String configPath, String serverPath, ServerConfig serverConfig, ServerData serverData) {
        this.zkClient = zkClient;
        this.configPath = configPath;
        this.serverPath = serverPath;
        this.serverConfig = serverConfig;
        this.serverData = serverData;
        this.iZkDataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("监听节点路径:"+dataPath+" 监听节点内容:"+new String((byte[])data));
                String ret=new String((byte[])data);//节点内容信息
                ServerConfig serverConfig1= (ServerConfig) JSONObject.parseObject(ret,ServerConfig.class);
                updateData(serverConfig1);//更新自己信息
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {

            }
        };
    }

    /*
     * @Description 程序启动入口
     * @Author zhangyu
     * @Date 2018/9/13 23:31
     * @Param []
     * @return void
     **/
    public void start(){
        initRunning();
    }

    /*
     * @Description 程序停止接口
     * @Author zhangyu
     * @Date 2018/9/13 23:34
     * @Param []
     * @return void
     **/
    public void stop(){
        zkClient.unsubscribeDataChanges(configPath,iZkDataListener);
    }

    /*
     * @Description 程序启动运行
     * @Author zhangyu
     * @Date 2018/9/13 23:32
     * @Param []
     * @return void
     **/
    private void initRunning(){
        registMe();
        zkClient.subscribeDataChanges(configPath,iZkDataListener);
    }

    /*
     * @Description 监听到配置节点内容改变后更新本机配置信息
     * @Author zhangyu
     * @Date 2018/9/13 23:18
     * @Param [serverConfig]
     * @return void
     **/
    private void updateData(ServerConfig serverConfig){
        this.serverConfig=serverConfig;
    }

    /*
     * @Description 注册本机信息 并且将本机信息创建server的临时节点
     * @Author zhangyu
     * @Date 2018/9/13 23:26
     * @Param []
     * @return void
     **/
    private void registMe(){
        String mePath = serverPath+"/"+serverData.getAddress();
        try {
            if (!zkClient.exists(mePath)){
                zkClient.createEphemeral(mePath,JSONObject.toJSONString(serverData).getBytes());
            }
        }catch (ZkNoNodeException e){
            zkClient.createPersistent(serverPath,true);
            registMe();
        }
    }
}

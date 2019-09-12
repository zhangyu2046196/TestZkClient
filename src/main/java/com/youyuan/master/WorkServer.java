package com.youyuan.master;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangyu
 * @version 1.0
 * @description zk应用场景之master选举
 * @date 2018/9/12 17:09
 */
public class WorkServer {

    //客户端连接状态
    private volatile boolean running=false;
    //zkClient
    private ZkClient zkClient;
    //监听master节点
    private String MASTER_PATH="/master";
    //监听事件，用于监听master节点删除事件
    private IZkDataListener iZkDataListener;
    //服务器基本信息
    private RunningData serverData;
    //主节点基本信息
    private RunningData masterData;
    //调度器
    private ScheduledExecutorService delyExecutor= Executors.newScheduledThreadPool(1);
    //延迟时间
    private Integer delyTime=5;

    public WorkServer(RunningData runningData){
        this.serverData=runningData;
        this.iZkDataListener=new IZkDataListener() {

            //监听dataPath节点,及节点数据的变化
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }
            //监听节点的删除事件
            public void handleDataDeleted(String dataPath) throws Exception {

                taskMaster();

                /*if (masterData!=null && masterData.getName().equals(serverData.getName())){
                    //若之前master为本机,则立即抢主,否则延迟5秒抢主(防止小故障引起的抢主可能导致的网络数据风暴)
                    taskMaster();//抢master
                }else{
                    //若之前master不是本机,则延迟5秒钟执行抢master程序
                    delyExecutor.schedule(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                taskMaster();//抢master
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },delyTime, TimeUnit.SECONDS);
                }*/
            }
        };
    }

    /*
     * @Description 启动
     * @Author zhangyu
     * @Date 2018/9/12 18:31
     * @Param []
     * @return void
     **/
    public void start() throws Exception {
        if (running){
            throw new Exception("已经有客户端连接");
        }
        running=true;
        zkClient.subscribeDataChanges(MASTER_PATH,iZkDataListener);//监听master节点删除事件
        taskMaster();
    }

    /*
     * @Description 停止
     * @Author zhangyu
     * @Date 2018/9/12 21:09
     * @Param []
     * @return void
     **/
    public void stop() throws Exception {
        if(!running){
            throw new Exception("没有客户端连接");
        }
        running=false;
        delyExecutor.shutdown();
        zkClient.subscribeDataChanges(MASTER_PATH,iZkDataListener);
        relaseMaster();
    }

    /*
     * @Description 抢主节点
     * @Author zhangyu
     * @Date 2018/9/12 17:55
     * @Param []
     * @return void
     **/
    private void taskMaster() throws Exception{
        if (!running){
            return;
        }
        try {
            zkClient.create(MASTER_PATH, serverData, CreateMode.EPHEMERAL);
            masterData = serverData;
            System.out.println(serverData.getName()+" is master");

            delyExecutor.schedule(new Runnable() {//测试抢主用,每5s释放一次主节点
                @Override
                public void run() {
                    if(checkMaster()){
                        relaseMaster();
                    }
                }
            },delyTime,TimeUnit.SECONDS);


        }catch (ZkNodeExistsException e){//节点已存在
            RunningData runningData = zkClient.readData(MASTER_PATH,true);
            if(runningData == null){//读取主节点时,主节点被释放
                taskMaster();
            }else{
                masterData = runningData;
            }
        } catch (Exception e) {
            // ignore;
        }
        /*boolean isExist=checkMaster();
        if (isExist){//master节点已经存在
            RunningData runningData=zkClient.readData(MASTER_PATH);
            if (runningData==null){
                taskMaster();
            }else{
                masterData=runningData;
            }
        }else{//master节点不存在
            String result=zkClient.create(MASTER_PATH,serverData, CreateMode.EPHEMERAL);
            System.out.println("创建master"+result);
            masterData=serverData;

*//*            delyExecutor.schedule(new Runnable() {//测试抢主,每5秒释放一次主节点
                @Override
                public void run() {
                    if (checkMaster()){
                        relaseMaster();
                    }
                }
            },delyTime,TimeUnit.SECONDS);*//*
        }
        System.out.println("主机id:"+masterData.getId()+" 主机名称:"+masterData.getName());*/
    }

    /*
     * @Description 释放主节点
     * @Author zhangyu
     * @Date 2018/9/12 17:17
     * @Param []
     * @return void
     **/
    private void relaseMaster(){
        if (checkMaster()){
            zkClient.delete(MASTER_PATH);//删除master节点
        }
    }

    /*
     * @Description 检查自己是否是主节点
     * @Author zhangyu
     * @Date 2018/9/12 17:18
     * @Param []
     * @return boolean true是主节点 false不是
     **/
    private boolean checkMaster(){
        boolean isExist=zkClient.exists(MASTER_PATH);
        if(!isExist){
            return false;
        }
        RunningData runningData= zkClient.readData(MASTER_PATH);
        //判断zk存储的节点名字是否和本机节点的名字一致，一致为本机节点就是master
        if (runningData!=null && runningData.getName().equals(serverData.getName())){
            return true;
        }
        return false;
    }

    public ZkClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }
}

package com.youyuan.master;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by nevermore on 16/6/22.
 */
public class WorkServer1 {

    //客户端状态
    private volatile boolean running = false;

    private ZkClient zkClient;

    //zk主节点路径
    public static final String MASTER_PATH = "/master";

    //监听(用于监听主节点删除事件)
    private IZkDataListener dataListener;

    //服务器基本信息
    private RunningData serverData;
    //主节点基本信息
    private RunningData masterData;

    //调度器
    private ScheduledExecutorService delayExector = Executors.newScheduledThreadPool(1);
    //延迟时间5s
    private int delayTime = 5;



    public WorkServer1(RunningData runningData){
        this.serverData = runningData;
        this.dataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                //takeMaster();

                if(masterData != null && masterData.getName().equals(serverData.getName())){//若之前master为本机,则立即抢主,否则延迟5秒抢主(防止小故障引起的抢主可能导致的网络数据风暴)
                    takeMaster();
                }else{
                    delayExector.schedule(new Runnable() {
                        @Override
                        public void run() {
                            takeMaster();
                        }
                    },delayTime, TimeUnit.SECONDS);
                }

            }
        };
    }

    //启动
    public void start() throws Exception{
        if(running){
            throw new Exception("server has startup....");
        }
        running = true;
        zkClient.subscribeDataChanges(MASTER_PATH,dataListener);
        takeMaster();
    }

    //停止
    public void stop() throws Exception{
        if(!running){
            throw new Exception("server has stopped.....");
        }
        running = false;
        delayExector.shutdown();
        zkClient.unsubscribeDataChanges(MASTER_PATH,dataListener);
        releaseMaster();
    }

    //抢注主节点
    private void takeMaster(){
        if(!running) return ;

        try {
            zkClient.create(MASTER_PATH, serverData, CreateMode.EPHEMERAL);
            masterData = serverData;
            System.out.println(serverData.getName()+" is master");

            delayExector.schedule(new Runnable() {//测试抢主用,每5s释放一次主节点
                @Override
                public void run() {
                    if(checkMaster()){
                        releaseMaster();
                    }
                }
            },5,TimeUnit.SECONDS);


        }catch (ZkNodeExistsException e){//节点已存在
            RunningData runningData = zkClient.readData(MASTER_PATH,true);
            if(runningData == null){//读取主节点时,主节点被释放
                takeMaster();
            }else{
                masterData = runningData;
            }
        } catch (Exception e) {
            // ignore;
        }

    }
    //释放主节点
    private void releaseMaster(){
        if(checkMaster()){
            zkClient.delete(MASTER_PATH);
        }
    }
    //检验自己是否是主节点
    private boolean checkMaster(){
        try {
            RunningData runningData = zkClient.readData(MASTER_PATH);
            masterData = runningData;
            if (masterData.getName().equals(serverData.getName())) {
                return true;
            }
            return false;

        }catch (ZkNoNodeException e){//节点不存在
            return  false;
        }catch (ZkInterruptedException e){//网络中断
            return checkMaster();
        }catch (Exception e){//其它
            return false;
        }
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    public ZkClient getZkClient() {
        return zkClient;
    }
}
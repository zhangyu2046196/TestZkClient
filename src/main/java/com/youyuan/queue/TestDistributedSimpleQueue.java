package com.youyuan.queue;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * @author zhangyu
 * @version 1.0
 * @description 模拟队列写数据和消费队列
 * @date 2018/9/19 20:17
 */
public class TestDistributedSimpleQueue {

    public static void main(String[] args) {


        ZkClient zkClient = new ZkClient("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181", 5000, 5000, new SerializableSerializer());
        DistributedSimpleQueue<User> queue = new DistributedSimpleQueue<User>(zkClient,"/Queue");

        User user1 = new User();
        user1.setId(1);
        user1.setName("xiao wang");

        User user2 = new User();
        user2.setId(2);
        user2.setName("xiao wang");

        try {
            queue.offer(user1);
            queue.offer(user2);
            User u1 = (User) queue.poll();
            User u2 = (User) queue.poll();

            System.out.println("u1:"+u1.toString());
            System.out.println("u2:"+u2.toString());

            if (user1.getId().intValue()==u1.getId().intValue() && user2.getId().intValue()==u2.getId().intValue()){
                System.out.println("Success!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

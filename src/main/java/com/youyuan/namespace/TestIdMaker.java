package com.youyuan.namespace;

/**
 * @author zhangyu
 * @version 1.0
 * @description 统一命名服务测试
 * @date 2018/9/25 10:05
 */
public class TestIdMaker {

    public static void main(String[] args) throws Exception {

        IdMaker idMaker = new IdMaker("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",
                "/NameService/IdGen", "ID");
        idMaker.start();

        try {
            for (int i = 0; i < 10; i++) {
                String id = idMaker.generateId(IdMaker.RemoveMethod.DELAY);
                System.out.println(id);
            }
        } finally {
            idMaker.stop();

        }
    }

}

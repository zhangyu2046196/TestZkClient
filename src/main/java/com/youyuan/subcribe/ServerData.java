package com.youyuan.subcribe;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangyu
 * @version 1.0
 * @description zk应用场景发布订阅,  此实体为work server的基本信息
 * @date 2018/9/13 22:59
 */
@Data
@AllArgsConstructor
public class ServerData implements Serializable {

    private static final long serialVersionUID = 5112442883533811620L;

    //服务器IP地址
    private String address;
    //服务器Id唯一标识
    private Integer id;
    //服务器名称
    private String name;
}

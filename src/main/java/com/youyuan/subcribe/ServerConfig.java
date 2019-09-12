package com.youyuan.subcribe;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangyu
 * @version 1.0
 * @description zk应用场景发布订阅,此实体为work server的配置信息
 * @date 2018/9/13 22:57
 */
@Data
@AllArgsConstructor
public class ServerConfig implements Serializable {

    private static final long serialVersionUID = 3729501132232854539L;

    //用户名
    private String userName;
    //密码
    private String passWord;
    //url地址信息
    private String url;
}

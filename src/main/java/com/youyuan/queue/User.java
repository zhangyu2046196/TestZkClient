package com.youyuan.queue;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangyu
 * @version 1.0
 * @description  实体对象
 * @date 2018/9/19 20:12
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 6388894327078248949L;
    //id
    private Integer id;
    //名字
    private String name;
}

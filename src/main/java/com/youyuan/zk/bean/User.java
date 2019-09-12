package com.youyuan.zk.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhangyu
 * @description
 * @date 2018/9/10 20:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -7108907437397711456L;
    private String userName;
    private Integer age;
}

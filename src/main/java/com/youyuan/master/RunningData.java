package com.youyuan.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhangyu
 * @version 1.0
 * @description zk应用场景之master选举 数据实体
 * @date 2018/9/12 17:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunningData implements Serializable {

    private static final long serialVersionUID = 2587770067793912364L;
    private String name;
    private Integer id;
}

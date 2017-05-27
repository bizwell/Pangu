package com.joindata.inf.registry.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 启动时记录的数据
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 26, 2017 1:21:55 PM
 */
@Data
@ToString
public class AppStartInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long timeCoast;

    private long memoryUsage;

    private long totalMemory;
}

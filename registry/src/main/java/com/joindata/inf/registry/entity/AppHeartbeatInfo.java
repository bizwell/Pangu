package com.joindata.inf.registry.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 心跳数据
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 31, 2017 12:04:21 PM
 */
@Data
@ToString
public class AppHeartbeatInfo implements Serializable
{
    private static final long serialVersionUID = 502297834466588631L;

    private long time;

    private long memoryUsage;

    private long totalMemory;

    public AppHeartbeatInfo()
    {
        this.setTime(System.currentTimeMillis());
        this.setMemoryUsage(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        this.setTotalMemory(Runtime.getRuntime().totalMemory());
    }
}

package com.joindata.inf.common.sterotype.mq;

import java.io.Serializable;

/**
 * 消息监听器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 16, 2017 2:54:38 PM
 */
public interface MessageListener<T extends Serializable>
{
    /**
     * 当收到一条消息时触发
     * 
     * @param message 接收到的消息
     */
    public void onReceive(T message);
}

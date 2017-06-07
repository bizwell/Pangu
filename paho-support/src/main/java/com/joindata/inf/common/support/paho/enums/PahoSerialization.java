package com.joindata.inf.common.support.paho.enums;

/**
 * Paho 队列特性
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jun 7, 2017 2:12:51 PM
 */
public enum PahoSerialization
{
    /** 用 JSON 序列化消息 */
    JSON,

    /** 纯粹字符串序列化消息 */
    String,

    /** 用 Java 序列化消息 */
    Java
}

package com.joindata.inf.common.support.rabbitmq.cst;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.util.basic.StringUtil;

/**
 * RabbitMQ 的一些默认值
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 14, 2017 11:17:47 AM
 */
public class RabbitDefault
{
    /** {@value #DEFAULT_ROUTING_KEY} */
    public static final String DEFAULT_ROUTING_KEY = "";

    /** {@value #DEFAULT_DIRECT_EXCHANGE} */
    public static final String DEFAULT_DIRECT_EXCHANGE = "amq.direct";

    /** {@value #DEFAULT_TOPIC_EXCHANGE} */
    public static final String DEFAULT_TOPIC_EXCHANGE = "amq.topic";

    /** {@value #DEFAULT_FANOUT_EXCHANGE} */
    public static final String DEFAULT_FANOUT_EXCHANGE = "amq.fanout";

    /** [系统ID].DIRECT */
    public static final String DEFAULT_SYS_DIRECT_EXCHANGE = StringUtil.splitToArray(BootInfoHolder.getAppId())[0] + ".DIRECT";

    /** [系统ID].TOPIC */
    public static final String DEFAULT_SYS_TOPIC_EXCHANGE = StringUtil.splitToArray(BootInfoHolder.getAppId())[0] + ".TOPIC";

    /** [系统ID].FANOUT */
    public static final String DEFAULT_SYS_FANOUT_EXCHANGE = StringUtil.splitToArray(BootInfoHolder.getAppId())[0] + ".FANOUT";
}

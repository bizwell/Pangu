package com.joindata.inf.common.support.rabbitmq.bootconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.rabbitmq.core.MessageListenerScanner;
import com.joindata.inf.common.support.rabbitmq.properties.RabbitMqProperties;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 配置 RabbitMQ
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 11, 2017 2:51:27 PM
 */
@Configuration
public class RabbitMqConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private RabbitMqProperties properties;

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

    @Bean
    public ConnectionFactory rabbitMqConnectionFactory()
    {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(properties.getHost());
        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());
        connectionFactory.setPort(properties.getPort());

        return connectionFactory;
    }

    @Bean
    public MessageListenerScanner messageListenerScanner()
    {
        return new MessageListenerScanner();
    }

    /**
     * 注册一大堆 Topic 二进制消息监听器
     */
    // @Bean(initMethod = "init")
    // public TopicMessageListenerHandler messageListenerHandler()
    // {
    // log.info("注册 RabbitMQ 消息监听器");
    //
    // new SimpleMessageListenerContainer().
    //
    // TopicMessageListenerHandler handler = new TopicMessageListenerHandler(rabbitMqConnectionFactory());
    // return handler;
    // }
}
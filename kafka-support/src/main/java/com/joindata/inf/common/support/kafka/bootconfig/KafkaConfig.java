package com.joindata.inf.common.support.kafka.bootconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.support.kafka.component.handler.BinaryMessageListenerHandler;
import com.joindata.inf.common.support.kafka.component.handler.StringMessageListenerHandler;
import com.joindata.inf.common.support.kafka.core.MessageListenerScanner;
import com.joindata.inf.common.support.kafka.properties.KafkaProperties;
import com.joindata.inf.common.util.log.Logger;

/**
 * 配置 Kafka
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 11, 2017 10:41:49 AM
 */
@Configuration
public class KafkaConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private KafkaProperties properties;

    // TODO
    private int receiveCount = 10;

    // TODO
    private int poolSize = 10;

    @Bean
    public MessageListenerScanner messageListenerScanner()
    {
        return new MessageListenerScanner();
    }

    /**
     * 注册一大堆 Topic 二进制消息监听器
     */
    @Bean(initMethod = "init")
    public BinaryMessageListenerHandler binaryMessageListenerHandler()
    {
        log.info("注册二进制消息监听器");
        BinaryMessageListenerHandler handler = new BinaryMessageListenerHandler(messageListenerScanner().scanBinaryTopicListener(), properties.getConsumeTimeout(), receiveCount, poolSize, properties.getBrokerList());
        return handler;
    }

    /**
     * 注册一大堆 Topic 文本消息监听器
     */
    @Bean(initMethod = "init")
    public StringMessageListenerHandler stringMessageListenerHandler()
    {
        log.info("注册文本消息监听器");
        StringMessageListenerHandler handler = new StringMessageListenerHandler(messageListenerScanner().scanStringTopicListener(), properties.getConsumeTimeout(), receiveCount, poolSize, properties.getBrokerList());
        return handler;
    }

}
package com.joindata.inf.common.support.rabbitmq.bootconfig;

import java.security.GeneralSecurityException;

import com.joindata.inf.zipkin.properties.ZipkinProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.basic.cst.PanguConfusing;
import com.joindata.inf.common.support.rabbitmq.core.MessageListenerScanner;
import com.joindata.inf.common.support.rabbitmq.core.handler.BroadcastMessageListenerHandler;
import com.joindata.inf.common.support.rabbitmq.core.handler.QueueMessageListenerHandler;
import com.joindata.inf.common.support.rabbitmq.core.handler.TopicMessageListenerHandler;
import com.joindata.inf.common.support.rabbitmq.properties.RabbitMqProperties;
import com.joindata.inf.common.util.basic.CodecUtil;
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
public class RabbitConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private RabbitMqProperties properties;

    @Autowired
    private ZipkinProperties zipkinProperties;

    @Bean
    public ConnectionFactory connectionFactory() throws GeneralSecurityException
    {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(properties.getHost());

        connectionFactory.setUsername(properties.getUsername());

        String password = properties.getPassword();
        if(StringUtil.startsWith(password, "enc("))
        {
            password = CodecUtil.decryptDES(StringUtil.substringBetweenFirstAndLast(password, "enc(", ")"), PanguConfusing.KEY);
        }

        connectionFactory.setPassword(password);
        connectionFactory.setPort(properties.getPort());
        connectionFactory.setVirtualHost(properties.getVirtualHost());

        log.info("创建 RabbitMQ 连接池, 主机: {}, 端口: {}, 虚拟主机: {}, 用户: {}", properties.getHost(), properties.getPort(), properties.getVirtualHost(), properties.getUsername());

        return connectionFactory;
    }

    @Bean
    public MessageListenerScanner messageListenerScanner()
    {
        return new MessageListenerScanner();
    }

    /**
     * 注册一大堆 Topic 消息监听器
     * 
     * @throws GeneralSecurityException
     */
    @Bean(initMethod = "startListener")
    public TopicMessageListenerHandler topicMessageListenerHandler() throws GeneralSecurityException
    {
        log.info("注册 RabbitMQ 主题消息监听器");
        return new TopicMessageListenerHandler(connectionFactory(), messageListenerScanner().scanTopicListener(), zipkinProperties.getKafkaServer());
    }

    /**
     * 注册一大堆直连消息监听器
     * 
     * @throws GeneralSecurityException
     */
    @Bean(initMethod = "startListener")
    public QueueMessageListenerHandler queueMessageListenerHandler() throws GeneralSecurityException
    {
        log.info("注册 RabbitMQ 直连消息监听器");
        return new QueueMessageListenerHandler(connectionFactory(), messageListenerScanner().scanQueueListener(), zipkinProperties.getKafkaServer());
    }

    /**
     * 注册一大堆广播消息监听器
     * 
     * @throws GeneralSecurityException
     */
    @Bean(initMethod = "startListener")
    public BroadcastMessageListenerHandler broadcastMessageListenerHandler() throws GeneralSecurityException
    {
        log.info("注册 RabbitMQ 广播消息监听器");
        return new BroadcastMessageListenerHandler(connectionFactory(), messageListenerScanner().scanBroadcastListener(), zipkinProperties.getKafkaServer());
    }
}
package com.joindata.inf.common.support.rabbitmq.component.handler;

import java.io.Serializable;
import java.util.Map;

import com.joindata.inf.common.sterotype.mq.MessageListener;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消息监听器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 12, 2017 1:38:31 PM
 */
public class TopicMessageListenerHandler
{
    private static final Logger log = Logger.get();

    /** queue - 消息监听器 */
    private Map<String, MessageListener<Serializable>> listenerMap;

    /** 连接工厂，通过 Spring Bean 初始化传进来 */
    private ConnectionFactory connectionFactory;

    /**
     * 构造二进制消息监听处理器
     */
    public TopicMessageListenerHandler(ConnectionFactory connectionFactory)
    {
        this.listenerMap = CollectionUtil.newMap();
        this.connectionFactory = connectionFactory;
    }

    // void init()
    // {
    // SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    // SimpleMessageListenerContainer listener = factory.createListenerContainer(new SimpleRabbitListenerEndpoint());
    // }
}

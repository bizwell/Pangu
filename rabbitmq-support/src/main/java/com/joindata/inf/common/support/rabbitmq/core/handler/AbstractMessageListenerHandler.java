package com.joindata.inf.common.support.rabbitmq.core.handler;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.joindata.inf.common.basic.entities.Pair;
import com.joindata.inf.common.basic.exceptions.ResourceException;
import com.joindata.inf.common.sterotype.mq.MessageListener;
import com.joindata.inf.common.support.rabbitmq.annotation.RabbitAttr;
import com.joindata.inf.common.support.rabbitmq.core.RabbitConsumer;
import com.joindata.inf.common.support.rabbitmq.enums.RabbitFeature;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.EnumUtil;
import com.joindata.inf.common.util.log.Logger;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;

/**
 * 消息监听器通用模板
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 14, 2017 9:57:12 AM
 */
public abstract class AbstractMessageListenerHandler
{
    private static final Logger log = Logger.get();

    private static final Map<String, Connection> ConnMap = CollectionUtil.newMap();

    private static final Map<Connection, Channel> ChannelMap = CollectionUtil.newMap();

    /** 连接工厂，通过 Spring Bean 初始化传进来 */
    private ConnectionFactory connectionFactory;

    /** queue - 消息监听器 */
    private Map<String, MessageListener<Serializable>> listenerMap;

    /** queue - 消费者监听器 */
    private Map<String, Consumer> consumerMap;

    /** queue - 队列设置 */
    private Map<String, RabbitAttr> configMap;

    /**
     * 构造二进制消息监听处理器
     */
    public AbstractMessageListenerHandler(ConnectionFactory connectionFactory, Map<String, MessageListener<Serializable>> listenerMap)
    {
        if(CollectionUtil.isNullOrEmpty(listenerMap))
        {
            listenerMap = CollectionUtil.newMap();
        }

        this.listenerMap = listenerMap;
        this.connectionFactory = connectionFactory;
        this.initConfigMap();
        this.initConsumerMap();
    }

    private void initConfigMap()
    {
        this.configMap = CollectionUtil.newMap();

        this.listenerMap.forEach((queue, listener) -> {
            this.configMap.put(queue, listener.getClass().getAnnotation(RabbitAttr.class));
        });
    }

    private void initConsumerMap()
    {
        consumerMap = CollectionUtil.newMap();

        this.listenerMap.keySet().forEach(queue -> {
            boolean isJson = EnumUtil.hasItem(this.getQueueConfig(queue).features(), RabbitFeature.JsonSerialization);
            consumerMap.put(queue, new RabbitConsumer<Serializable>(queue, isJson, this.getListener(queue)));
        });
    }

    /**
     * 获取指定队列的 Listener
     * 
     * @param queue 队列名
     * @return Listener
     */
    protected MessageListener<Serializable> getListener(String queue)
    {
        return this.listenerMap.get(queue);
    }

    protected Map<String, MessageListener<Serializable>> getListenerMap()
    {
        return this.listenerMap;
    }

    protected Consumer getConsumer(String queue)
    {
        return this.consumerMap.get(queue);
    }

    protected RabbitAttr getQueueConfig(String queue)
    {
        return this.configMap.get(queue);
    }

    /**
     * 获取通道<br />
     * <i>如果被关闭或尚未创建，会尝试创建</i>
     * 
     * @param queue 该连接所属队列
     * @return 该队列所属通道
     */
    @SuppressWarnings("unchecked")
    public Pair<Connection, Channel> getChannel(String queue)
    {
        Connection conn = getConnection(queue);
        if(!ChannelMap.containsKey(conn) || ChannelMap.get(conn) == null)
        {
            try
            {
                log.info("创建队列 {} 的通道", queue);
                ChannelMap.put(conn, conn.createChannel());
            }
            catch(IOException e)
            {
                log.error("创建队列 {} 的通道出错: {}", queue, e.getMessage(), e);
            }
        }

        Channel channel = ChannelMap.get(conn);
        if(!channel.isOpen())
        {
            log.debug("队列 {} 的通道已经被关闭, 原因: {}", queue, channel.getCloseReason().getMessage());

            try
            {
                log.debug("重新创建队列 {} 的通道", queue);
                ChannelMap.put(conn, conn.createChannel());
            }
            catch(IOException e)
            {
                log.error("重新创建队列 {} 的通道出错: {}", queue, e.getMessage(), e);
                throw new ResourceException("无法取到 MQ 通道");
            }
        }

        return Pair.define(conn, channel);
    }

    /**
     * 获取连接<br />
     * <i>如果被关闭或尚未创建，会尝试创建</i>
     * 
     * @param queue 该连接所属队列
     * @return 该队列所属连接
     */
    public Connection getConnection(String queue)
    {
        // 如果连接不存，创建连接
        if(!ConnMap.containsKey(queue) || ConnMap.get(queue) == null)
        {
            try
            {
                log.info("创建队列 {} 的连接", queue);
                ConnMap.put(queue, connectionFactory.newConnection());
            }
            catch(IOException | TimeoutException e)
            {
                log.error("创建队列 {} 的连接出错: {}", queue, e.getMessage(), e);
            }
        }

        Connection conn = ConnMap.get(queue);
        if(!conn.isOpen())
        {
            log.debug("队列 {} 的连接已经被关闭, 原因: {}", queue, conn.getCloseReason().getMessage());

            try
            {
                log.debug("重新创建队列 {} 的连接", queue);
                ConnMap.put(queue, connectionFactory.newConnection());
            }
            catch(IOException | TimeoutException e)
            {
                log.error("重新创建队列 {} 的连接出错: {}", queue, e.getMessage(), e);
                throw new ResourceException("无法取到 MQ 连接");
            }
        }

        return conn;
    }

    public abstract void startListener();
}

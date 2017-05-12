package com.joindata.inf.common.support.rabbitmq.component;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joindata.inf.common.basic.entities.Pair;
import com.joindata.inf.common.basic.exceptions.ResourceException;
import com.joindata.inf.common.support.rabbitmq.cst.RabbitDefault;
import com.joindata.inf.common.support.rabbitmq.enums.RabbitFeature;
import com.joindata.inf.common.util.basic.BeanUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.EnumUtil;
import com.joindata.inf.common.util.basic.JsonUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * RabbitMQ 客户端<br />
 * <p>
 * 提供了一部分常用的发送、接收方法，如果无法涵盖到需求，可通过 {@link #getConnection(String)} 来获取指定队列的连接，或者通过 {@link #getChannel(String)} 来获取指定队列 的Channel，从而可以做进一步的自定义操作
 * </p>
 * <br />
 * <strong>注意: </strong>本类中提供的简单发送、接收方法的连接均是长连接，一旦创建如果不主动关闭将会一直存在，请依据情况使用
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 11, 2017 3:09:43 PM
 */
@Component
public class RabbitClient
{
    private static final Logger log = Logger.get();

    private static final Map<String, Connection> ConnMap = CollectionUtil.newMap();

    private static final Map<Connection, Channel> ChannelMap = CollectionUtil.newMap();

    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * 发送直达消息
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param directExchange 绑定交换机名
     * @param routingKey 路由键
     * @param feature 队列设置
     */
    public void sendDirect(String queue, String content, String directExchange, String routingKey, RabbitFeature... feature)
    {
        directExchange = directExchange == null ? RabbitDefault.DEFAULT_DIRECT_EXCHANGE : directExchange;
        routingKey = routingKey == null ? RabbitDefault.DEFAULT_ROUTING_KEY : routingKey;

        log.info("发送直接消息到 {}, 绑定交换机: {}, 路由键: {}", queue, directExchange, routingKey.equals(RabbitDefault.DEFAULT_ROUTING_KEY) ? "[默认]" : routingKey);
        Pair<Connection, Channel> pair = getChannel(queue);
        Channel channel = pair.getValue();

        try
        {
            if(StringUtil.isNotEmpty(directExchange))
            {
                boolean durable = !EnumUtil.hasItem(feature, RabbitFeature.ExchangeTransient);
                boolean autoDelete = EnumUtil.hasItem(feature, RabbitFeature.ExchangeAutoDelete);
                boolean internal = EnumUtil.hasItem(feature, RabbitFeature.ExchangeInternal);

                channel.exchangeDeclare(directExchange, BuiltinExchangeType.DIRECT, durable, autoDelete, internal, null);
            }

            {

                boolean durable = !EnumUtil.hasItem(feature, RabbitFeature.QueueTransient);
                boolean exclusive = EnumUtil.hasItem(feature, RabbitFeature.QueueExclusive);
                boolean autoDelete = EnumUtil.hasItem(feature, RabbitFeature.QueueAutoDelete);

                channel.queueDeclare(queue, durable, exclusive, autoDelete, null);
            }

            channel.basicPublish(directExchange, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, StringUtil.toBytes(content));
        }
        catch(IOException e)
        {
            log.error("发送队列到 {} 的消息出错: {}", queue, e.getMessage(), e);
        }
    }

    /**
     * 发送直达消息<br />
     * <i>路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param directExchange 绑定交换机名
     * @param feature 队列设置
     */
    public void sendDirect(String queue, String content, String directExchange, RabbitFeature... feature)
    {
        sendDirect(queue, content, directExchange, null, feature);
    }

    /**
     * 发送直达消息<br />
     * <i>绑定交换机名和路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param feature 队列设置
     */
    public void sendDirect(String queue, String content, RabbitFeature... feature)
    {
        sendDirect(queue, content, null, null, feature);
    }

    /**
     * 发送广播消息
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param fanoutExchange 绑定交换机名
     * @param routingKey 路由键
     * @param feature 队列设置
     */
    public void sendBroadcastBindQueue(String queue, String content, String fanoutExchange, String routingKey, RabbitFeature... feature)
    {
        fanoutExchange = fanoutExchange == null ? RabbitDefault.DEFAULT_FANOUT_EXCHANGE : fanoutExchange;
        routingKey = routingKey == null ? RabbitDefault.DEFAULT_ROUTING_KEY : routingKey;

        log.info("发送广播消息到 {}, 绑定交换机: {}, 路由键: {}", queue, fanoutExchange, routingKey.equals(RabbitDefault.DEFAULT_ROUTING_KEY) ? "[默认]" : routingKey);
        Pair<Connection, Channel> pair = getChannel(queue);
        Channel channel = pair.getValue();

        try
        {

            if(StringUtil.isNotEmpty(fanoutExchange))
            {
                boolean durable = !EnumUtil.hasItem(feature, RabbitFeature.ExchangeTransient);
                boolean autoDelete = EnumUtil.hasItem(feature, RabbitFeature.ExchangeAutoDelete);
                boolean internal = EnumUtil.hasItem(feature, RabbitFeature.ExchangeInternal);

                channel.exchangeDeclare(fanoutExchange, BuiltinExchangeType.FANOUT, durable, autoDelete, internal, null);
            }

            {
                boolean durable = !EnumUtil.hasItem(feature, RabbitFeature.QueueTransient);
                boolean exclusive = EnumUtil.hasItem(feature, RabbitFeature.QueueExclusive);
                boolean autoDelete = EnumUtil.hasItem(feature, RabbitFeature.QueueAutoDelete);

                channel.queueDeclare(queue, durable, exclusive, autoDelete, null);
            }

            channel.basicPublish(fanoutExchange, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, StringUtil.toBytes(content));
        }
        catch(IOException e)
        {
            log.error("发送广播到 {} 的消息出错: {}", queue, e.getMessage(), e);
        }
    }

    /**
     * 发送广播消息<br />
     * <strong>不设定绑定队列</strong>
     * 
     * @param content 消息内容
     * @param fanoutExchange 绑定交换机名
     * @param routingKey 路由键
     * @param feature 队列设置
     */
    public void sendBroadcast(String content, String fanoutExchange, String routingKey, RabbitFeature... feature)
    {
        fanoutExchange = fanoutExchange == null ? RabbitDefault.DEFAULT_FANOUT_EXCHANGE : fanoutExchange;
        routingKey = routingKey == null ? RabbitDefault.DEFAULT_ROUTING_KEY : routingKey;

        log.info("发送广播消息, 绑定交换机: {}, 路由键: {}", fanoutExchange, routingKey.equals(RabbitDefault.DEFAULT_ROUTING_KEY) ? "[默认]" : routingKey);
        Pair<Connection, Channel> pair = getChannel(fanoutExchange + "$GLOBAL_BROADCAST");
        Channel channel = pair.getValue();

        try
        {

            if(StringUtil.isNotEmpty(fanoutExchange))
            {
                boolean durable = !EnumUtil.hasItem(feature, RabbitFeature.ExchangeTransient);
                boolean autoDelete = EnumUtil.hasItem(feature, RabbitFeature.ExchangeAutoDelete);
                boolean internal = EnumUtil.hasItem(feature, RabbitFeature.ExchangeInternal);

                channel.exchangeDeclare(fanoutExchange, BuiltinExchangeType.FANOUT, durable, autoDelete, internal, null);
            }

            channel.basicPublish(fanoutExchange, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, StringUtil.toBytes(content));
        }
        catch(IOException e)
        {
            log.error("发送广播到交换机 {} 的消息出错: {}", fanoutExchange, e.getMessage(), e);
        }
    }

    /**
     * 发送广播消息<br />
     * <strong>不绑定队列</strong>， <i>路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param fanoutExchange 绑定交换机名
     * @param feature 队列设置
     */
    public void sendBroadcast(String content, String fanoutExchange, RabbitFeature... feature)
    {
        sendBroadcast(content, fanoutExchange, null, feature);
    }

    /**
     * 发送广播消息<br />
     * <strong>不绑定队列</strong>， <i>绑定交换机名和路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param feature 队列设置
     */
    public void sendBroadcast(String content, RabbitFeature... feature)
    {
        sendBroadcast(content, null, null, feature);
    }

    /**
     * 发送广播消息<br />
     * <i>路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param fanoutExchange 绑定交换机名
     * @param feature 队列设置
     */
    public void sendBroadcastBindQueue(String queue, String content, String fanoutExchange, RabbitFeature... feature)
    {
        sendBroadcastBindQueue(queue, content, fanoutExchange, null, feature);
    }

    /**
     * 发送广播消息<br />
     * <i>绑定交换机名和路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param feature 队列设置
     */
    public void sendBroadcastBindQueue(String queue, String content, RabbitFeature... feature)
    {
        sendBroadcastBindQueue(queue, content, null, null, feature);
    }

    /**
     * 发送主题消息
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param topicExchange 绑定交换机名
     * @param routingKey 路由键
     * @param feature 队列设置
     */
    public void sendTopic(String queue, String content, String topicExchange, String routingKey, RabbitFeature... feature)
    {
        topicExchange = topicExchange == null ? RabbitDefault.DEFAULT_TOPIC_EXCHANGE : topicExchange;
        routingKey = routingKey == null ? RabbitDefault.DEFAULT_ROUTING_KEY : routingKey;

        log.info("发送主题消息到 {}, 绑定交换机: {}, 路由键: {}", queue, topicExchange, routingKey.equals(RabbitDefault.DEFAULT_ROUTING_KEY) ? "[默认]" : routingKey);
        Pair<Connection, Channel> pair = getChannel(queue);
        Channel channel = pair.getValue();

        try
        {
            if(StringUtil.isNotEmpty(topicExchange))
            {
                boolean durable = !EnumUtil.hasItem(feature, RabbitFeature.ExchangeTransient);
                boolean autoDelete = EnumUtil.hasItem(feature, RabbitFeature.ExchangeAutoDelete);
                boolean internal = EnumUtil.hasItem(feature, RabbitFeature.ExchangeInternal);

                channel.exchangeDeclare(topicExchange, BuiltinExchangeType.TOPIC, durable, autoDelete, internal, null);
            }

            {
                boolean durable = !EnumUtil.hasItem(feature, RabbitFeature.QueueTransient);
                boolean exclusive = EnumUtil.hasItem(feature, RabbitFeature.QueueExclusive);
                boolean autoDelete = EnumUtil.hasItem(feature, RabbitFeature.QueueAutoDelete);
                channel.queueDeclare(queue, durable, exclusive, autoDelete, null);

            }
            channel.basicPublish(topicExchange, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, StringUtil.toBytes(content));
        }
        catch(IOException e)
        {
            log.error("发送主题到 {} 的消息出错: {}", queue, e.getMessage(), e);
        }
    }

    /**
     * 发送主题消息<br />
     * <i>路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param topicExchange 绑定交换机名
     * @param feature 队列设置
     */
    public void sendTopic(String queue, String content, String topicExchange, RabbitFeature... feature)
    {
        sendTopic(queue, content, topicExchange, null, feature);
    }

    /**
     * 发送主题消息<br />
     * <i>绑定交换机名和路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param feature 队列设置
     */
    public void sendTopic(String queue, String content, RabbitFeature... feature)
    {
        sendTopic(queue, content, null, null, feature);
    }

    /**
     * 发送直达消息
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param directExchange 绑定交换机名
     * @param routingKey 路由键
     * @param feature 队列设置
     */
    public void sendDirect(String queue, Serializable content, String directExchange, String routingKey, RabbitFeature... feature)
    {
        // 如果用 JSON，直接算字符串的
        if(EnumUtil.hasItem(feature, RabbitFeature.JsonSerialization))
        {
            sendDirect(queue, JsonUtil.toJSON(content), directExchange, routingKey, feature);
            return;
        }

        directExchange = directExchange == null ? RabbitDefault.DEFAULT_DIRECT_EXCHANGE : directExchange;
        routingKey = routingKey == null ? RabbitDefault.DEFAULT_ROUTING_KEY : routingKey;

        log.info("发送直接消息到 {}, 绑定交换机: {}, 路由键: {}", queue, directExchange, routingKey.equals(RabbitDefault.DEFAULT_ROUTING_KEY) ? "[默认]" : routingKey);
        Pair<Connection, Channel> pair = getChannel(queue);
        Channel channel = pair.getValue();

        try
        {
            if(StringUtil.isNotEmpty(directExchange))
            {
                boolean durable = !EnumUtil.hasItem(feature, RabbitFeature.ExchangeTransient);
                boolean autoDelete = EnumUtil.hasItem(feature, RabbitFeature.ExchangeAutoDelete);
                boolean internal = EnumUtil.hasItem(feature, RabbitFeature.ExchangeInternal);

                channel.exchangeDeclare(directExchange, BuiltinExchangeType.DIRECT, durable, autoDelete, internal, null);
            }

            {

                boolean durable = !EnumUtil.hasItem(feature, RabbitFeature.QueueTransient);
                boolean exclusive = EnumUtil.hasItem(feature, RabbitFeature.QueueExclusive);
                boolean autoDelete = EnumUtil.hasItem(feature, RabbitFeature.QueueAutoDelete);

                channel.queueDeclare(queue, durable, exclusive, autoDelete, null);
            }

            channel.basicPublish(directExchange, routingKey, null, BeanUtil.serializeObject(content));
        }
        catch(IOException e)
        {
            log.error("发送队列到 {} 的消息出错: {}", queue, e.getMessage(), e);
        }
    }

    /**
     * 发送直达消息<br />
     * <i>路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param directExchange 绑定交换机名
     * @param feature 队列设置
     */
    public void sendDirect(String queue, Serializable content, String directExchange, RabbitFeature... feature)
    {
        sendDirect(queue, content, directExchange, null, feature);
    }

    /**
     * 发送直达消息<br />
     * <i>绑定交换机名和路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param feature 队列设置
     */
    public void sendDirect(String queue, Serializable content, RabbitFeature... feature)
    {
        sendDirect(queue, content, null, null, feature);
    }

    /**
     * 发送广播消息
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param fanoutExchange 绑定交换机名
     * @param routingKey 路由键
     * @param feature 队列设置
     */
    public void sendBroadcastBindQueue(String queue, Serializable content, String fanoutExchange, String routingKey, RabbitFeature... feature)
    {
        // 如果用 JSON，直接算字符串的
        if(EnumUtil.hasItem(feature, RabbitFeature.JsonSerialization))
        {
            sendBroadcastBindQueue(queue, JsonUtil.toJSON(content), fanoutExchange, routingKey, feature);
            return;
        }

        fanoutExchange = fanoutExchange == null ? RabbitDefault.DEFAULT_FANOUT_EXCHANGE : fanoutExchange;
        routingKey = routingKey == null ? RabbitDefault.DEFAULT_ROUTING_KEY : routingKey;

        log.info("发送广播消息到 {}, 绑定交换机: {}, 路由键: {}", queue, fanoutExchange, routingKey.equals(RabbitDefault.DEFAULT_ROUTING_KEY) ? "[默认]" : routingKey);
        Pair<Connection, Channel> pair = getChannel(queue);
        Channel channel = pair.getValue();

        try
        {

            if(StringUtil.isNotEmpty(fanoutExchange))
            {
                boolean durable = !EnumUtil.hasItem(feature, RabbitFeature.ExchangeTransient);
                boolean autoDelete = EnumUtil.hasItem(feature, RabbitFeature.ExchangeAutoDelete);
                boolean internal = EnumUtil.hasItem(feature, RabbitFeature.ExchangeInternal);

                channel.exchangeDeclare(fanoutExchange, BuiltinExchangeType.FANOUT, durable, autoDelete, internal, null);
            }

            {
                boolean durable = !EnumUtil.hasItem(feature, RabbitFeature.QueueTransient);
                boolean exclusive = EnumUtil.hasItem(feature, RabbitFeature.QueueExclusive);
                boolean autoDelete = EnumUtil.hasItem(feature, RabbitFeature.QueueAutoDelete);

                channel.queueDeclare(queue, durable, exclusive, autoDelete, null);
            }

            channel.basicPublish(fanoutExchange, routingKey, null, BeanUtil.serializeObject(content));
        }
        catch(IOException e)
        {
            log.error("发送广播到 {} 的消息出错: {}", queue, e.getMessage(), e);
        }
    }

    /**
     * 发送广播消息<br />
     * <strong>不绑定队列</strong>
     * 
     * @param content 消息内容
     * @param fanoutExchange 绑定交换机名
     * @param routingKey 路由键
     * @param feature 队列设置
     */
    public void sendBroadcast(Serializable content, String fanoutExchange, String routingKey, RabbitFeature... feature)
    {
        // 如果用 JSON，直接算字符串的
        if(EnumUtil.hasItem(feature, RabbitFeature.JsonSerialization))
        {
            sendBroadcast(JsonUtil.toJSON(content), fanoutExchange, routingKey, feature);
            return;
        }

        fanoutExchange = fanoutExchange == null ? RabbitDefault.DEFAULT_FANOUT_EXCHANGE : fanoutExchange;
        routingKey = routingKey == null ? RabbitDefault.DEFAULT_ROUTING_KEY : routingKey;

        log.info("发送广播消息, 绑定交换机: {}, 路由键: {}", fanoutExchange, routingKey.equals(RabbitDefault.DEFAULT_ROUTING_KEY) ? "[默认]" : routingKey);
        Pair<Connection, Channel> pair = getChannel(fanoutExchange + "$GLOBAL_BROADCAST");
        Channel channel = pair.getValue();

        try
        {

            if(StringUtil.isNotEmpty(fanoutExchange))
            {
                boolean durable = !EnumUtil.hasItem(feature, RabbitFeature.ExchangeTransient);
                boolean autoDelete = EnumUtil.hasItem(feature, RabbitFeature.ExchangeAutoDelete);
                boolean internal = EnumUtil.hasItem(feature, RabbitFeature.ExchangeInternal);

                channel.exchangeDeclare(fanoutExchange, BuiltinExchangeType.FANOUT, durable, autoDelete, internal, null);
            }

            channel.basicPublish(fanoutExchange, routingKey, null, BeanUtil.serializeObject(content));
        }
        catch(IOException e)
        {
            log.error("发送广播到交换机 {} 的消息出错: {}", fanoutExchange, e.getMessage(), e);
        }
    }

    /**
     * 发送广播消息<br />
     * <i>路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param fanoutExchange 绑定交换机名
     * @param feature 队列设置
     */
    public void sendBroadcastBindQueue(String queue, Serializable content, String fanoutExchange, RabbitFeature... feature)
    {
        sendBroadcastBindQueue(queue, content, fanoutExchange, null, feature);
    }

    /**
     * 发送广播消息<br />
     * <i>绑定交换机名和路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param feature 队列设置
     */
    public void sendBroadcastBindQueue(String queue, Serializable content, RabbitFeature... feature)
    {
        sendBroadcastBindQueue(queue, content, null, null, feature);
    }

    /**
     * 发送广播消息<br />
     * <strong>不绑定队列，</strong> <i>路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param fanoutExchange 绑定交换机名
     * @param feature 队列设置
     */
    public void sendBroadcast(Serializable content, String fanoutExchange, RabbitFeature... feature)
    {
        sendBroadcast(content, fanoutExchange, null, feature);
    }

    /**
     * 发送广播消息<br />
     * <strong>不绑定队列，</strong> <i>绑定交换机名和路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param feature 队列设置
     */
    public void sendBroadcast(Serializable content, RabbitFeature... feature)
    {
        sendBroadcast(content, null, null, feature);
    }

    /**
     * 发送主题消息
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param topicExchange 绑定交换机名
     * @param routingKey 路由键
     * @param feature 队列设置
     */
    public void sendTopic(String queue, Serializable content, String topicExchange, String routingKey, RabbitFeature... feature)
    {
        // 如果用 JSON，直接算字符串的
        if(EnumUtil.hasItem(feature, RabbitFeature.JsonSerialization))
        {
            sendTopic(queue, JsonUtil.toJSON(content), topicExchange, routingKey, feature);
            return;
        }

        topicExchange = topicExchange == null ? RabbitDefault.DEFAULT_TOPIC_EXCHANGE : topicExchange;
        routingKey = routingKey == null ? RabbitDefault.DEFAULT_ROUTING_KEY : routingKey;

        log.info("发送主题消息到 {}, 绑定交换机: {}, 路由键: {}", queue, topicExchange, routingKey.equals(RabbitDefault.DEFAULT_ROUTING_KEY) ? "[默认]" : routingKey);
        Pair<Connection, Channel> pair = getChannel(queue);
        Channel channel = pair.getValue();

        try
        {
            if(StringUtil.isNotEmpty(topicExchange))
            {
                boolean durable = !EnumUtil.hasItem(feature, RabbitFeature.ExchangeTransient);
                boolean autoDelete = EnumUtil.hasItem(feature, RabbitFeature.ExchangeAutoDelete);
                boolean internal = EnumUtil.hasItem(feature, RabbitFeature.ExchangeInternal);

                channel.exchangeDeclare(topicExchange, BuiltinExchangeType.TOPIC, durable, autoDelete, internal, null);
            }

            {
                boolean durable = !EnumUtil.hasItem(feature, RabbitFeature.QueueTransient);
                boolean exclusive = EnumUtil.hasItem(feature, RabbitFeature.QueueExclusive);
                boolean autoDelete = EnumUtil.hasItem(feature, RabbitFeature.QueueAutoDelete);
                channel.queueDeclare(queue, durable, exclusive, autoDelete, null);

            }
            channel.basicPublish(topicExchange, routingKey, null, BeanUtil.serializeObject(content));
        }
        catch(IOException e)
        {
            log.error("发送主题到 {} 的消息出错: {}", queue, e.getMessage(), e);
        }
    }

    /**
     * 发送主题消息<br />
     * <i>路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param topicExchange 绑定交换机名
     * @param feature 队列设置
     */
    public void sendTopic(String queue, Serializable content, String topicExchange, RabbitFeature... feature)
    {
        sendTopic(queue, content, topicExchange, null, feature);
    }

    /**
     * 发送主题消息<br />
     * <i>绑定交换机名和路由键用默认的</i>
     * 
     * @param queue 队列名
     * @param content 消息内容
     * @param feature 队列设置
     */
    public void sendTopic(String queue, Serializable content, RabbitFeature... feature)
    {
        sendTopic(queue, content, null, null, feature);
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
}

package com.joindata.inf.common.support.rabbitmq.core;

import java.io.IOException;
import java.io.Serializable;

import com.joindata.inf.common.sterotype.mq.MessageListener;
import com.joindata.inf.common.support.rabbitmq.enums.SerializationType;
import com.joindata.inf.common.util.basic.BeanUtil;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.JsonUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * 自定义的 Consumer
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 14, 2017 10:30:31 AM
 */
public class RabbitConsumer<T extends Serializable> implements Consumer
{
    private static final Logger log = Logger.get();

    private MessageListener<T> listener;

    private String queue;

    private Class<?> dataClz;

    private SerializationType deserialization;

    public RabbitConsumer(String queue, boolean isJson, MessageListener<T> listener)
    {
        this.queue = queue;
        this.listener = listener;
        this.dataClz = ClassUtil.getNestedGenericType(listener.getClass());

        if(isJson)
        {
            deserialization = SerializationType.JSON;
        }
        else if(this.dataClz.equals(String.class))
        {
            deserialization = SerializationType.STRING;
        }
        else
        {
            deserialization = SerializationType.JAVA;
        }
    }

    @Override
    public void handleConsumeOk(String consumerTag)
    {
        log.info("收取 {} 的消息成功, 消费器: {}", queue, consumerTag);
    }

    @Override
    public void handleCancelOk(String consumerTag)
    {
        log.info("取消 {} 的消息成功, 消费器: {}", queue, consumerTag);
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException
    {
        log.info("取消 {} 的消息, 消费器: {}", queue, consumerTag);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
    {
        log.info("收取 {} 的消息, 大小: {}, 序列化方式: {}", queue, properties.getBodySize(), deserialization);
        log.debug("收取 {} 消息的属性: {}, 消费器: {}", queue, properties, consumerTag);
        log.debug("收取 {} 环境的属性: {}, 消费器: {}", queue, envelope, consumerTag);

        T msg = null;
        try
        {
            switch(deserialization)
            {
                case JSON:
                    msg = (T)JsonUtil.fromJSON(StringUtil.toString(body), this.dataClz);
                    break;
                case STRING:
                    msg = (T)StringUtil.toString(body);
                    break;
                default:
                    msg = BeanUtil.deserializeObject(body);
            }
        }
        catch(Exception e)
        {
            log.error("序列化消息时出错: {}", e.getMessage(), e);
            return;
        }

        try
        {
            this.listener.onReceive(msg);
        }
        catch(Exception e)
        {
            log.error("接收消息时出错: {}", e.getMessage(), e);
        }
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig)
    {
        log.warn("结束 {} 的消息收取, 消费器: {}", queue, consumerTag);
    }

    @Override
    public void handleRecoverOk(String consumerTag)
    {
        log.info("恢复 {} 的消息成功, 消费器: {}", queue, consumerTag);
    }
}

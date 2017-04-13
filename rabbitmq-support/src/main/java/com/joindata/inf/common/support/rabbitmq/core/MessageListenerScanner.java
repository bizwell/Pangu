package com.joindata.inf.common.support.rabbitmq.core;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.basic.support.SpringContextHolder;
import com.joindata.inf.common.sterotype.mq.MessageListener;
import com.joindata.inf.common.sterotype.mq.annotation.Queue;
import com.joindata.inf.common.sterotype.mq.annotation.Topic;
import com.joindata.inf.common.support.rabbitmq.annotation.RabbitQueue;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;

public class MessageListenerScanner
{
    private static final Logger log = Logger.get();

    private final Class<Topic> topicClz = Topic.class;

    private final Class<Queue> queueClz = Queue.class;

    private final Class<RabbitQueue> rabbitQueueClz = RabbitQueue.class;

    @SuppressWarnings("unchecked")
    public Map<String, MessageListener<Serializable>> scanTopicListener()
    {
        Map<String, MessageListener<Serializable>> map = CollectionUtil.newMap();

        Set<Class<?>> clzSet = ClassUtil.scanTypeAnnotations(BootInfoHolder.getAppPackage(), topicClz);

        for(Class<?> clz: clzSet)
        {
            Topic topic = clz.getAnnotation(topicClz);

            if(!MessageListener.class.isAssignableFrom(clz))
            {
                continue;
            }

            Class<Serializable> innerClz = ClassUtil.getNestedGenericType(clz);
            if(String.class.equals(innerClz))
            {
                continue;
            }

            log.info("注册 RabbitMQ 消息监听器, 监听主题: {}", topic.value());

            map.put(clz.getAnnotation(topicClz).value(), (MessageListener<Serializable>)SpringContextHolder.getBean(clz));
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    public Map<String, MessageListener<Serializable>> scanQueueListener()
    {
        Map<String, MessageListener<Serializable>> map = CollectionUtil.newMap();

        Set<Class<?>> clzSet = ClassUtil.scanTypeAnnotations(BootInfoHolder.getAppPackage(), queueClz);

        for(Class<?> clz: clzSet)
        {
            Queue queue = clz.getAnnotation(queueClz);
            RabbitQueue rabbitQueue = clz.getAnnotation(rabbitQueueClz);
            if(rabbitQueue == null)
            {
                log.debug("不是 RabbigMQ 的监听器: {}, 略过", queue.value());
                continue;
            }

            if(!MessageListener.class.isAssignableFrom(clz))
            {
                continue;
            }

            Class<Serializable> innerClz = ClassUtil.getNestedGenericType(clz);
            if(String.class.equals(innerClz))
            {
                continue;
            }

            log.info("注册 RabbitMQ 消息监听器, 监听队列: {}", queue.value());

            map.put(clz.getAnnotation(queueClz).value(), (MessageListener<Serializable>)SpringContextHolder.getBean(clz));
        }

        return map;
    }
}

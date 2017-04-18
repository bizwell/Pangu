package com.joindata.inf.common.support.rabbitmq.core;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.basic.support.SpringContextHolder;
import com.joindata.inf.common.sterotype.mq.MessageListener;
import com.joindata.inf.common.sterotype.mq.annotation.Broadcast;
import com.joindata.inf.common.sterotype.mq.annotation.Queue;
import com.joindata.inf.common.sterotype.mq.annotation.Topic;
import com.joindata.inf.common.support.rabbitmq.annotation.RabbitAttr;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;

public class MessageListenerScanner
{
    private static final Logger log = Logger.get();

    private final Class<Topic> topicClz = Topic.class;

    private final Class<Queue> queueClz = Queue.class;

    private final Class<Broadcast> broadcastClz = Broadcast.class;

    private final Class<RabbitAttr> rabbitQueueClz = RabbitAttr.class;

    @SuppressWarnings("unchecked")
    public Map<String, MessageListener<Serializable>> scanTopicListener()
    {
        Map<String, MessageListener<Serializable>> map = CollectionUtil.newMap();

        Set<Class<?>> clzSet = ClassUtil.scanTypeAnnotations(BootInfoHolder.getAppPackage(), topicClz);

        for(Class<?> clz: clzSet)
        {
            Topic topic = clz.getAnnotation(topicClz);
            RabbitAttr rabbitAttr = clz.getAnnotation(rabbitQueueClz);
            if(rabbitAttr == null)
            {
                log.debug("不是 RabbigMQ 的监听器: {}, 略过", topic.value());
                continue;
            }

            if(!MessageListener.class.isAssignableFrom(clz))
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
            RabbitAttr rabbitAttr = clz.getAnnotation(rabbitQueueClz);
            if(rabbitAttr == null)
            {
                log.debug("不是 RabbigMQ 的监听器: {}, 略过", queue.value());
                continue;
            }

            if(!MessageListener.class.isAssignableFrom(clz))
            {
                continue;
            }

            log.info("注册 RabbitMQ 消息监听器, 监听队列: {}", queue.value());

            map.put(clz.getAnnotation(queueClz).value(), (MessageListener<Serializable>)SpringContextHolder.getBean(clz));
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    public Map<String, MessageListener<Serializable>> scanBroadcastListener()
    {
        Map<String, MessageListener<Serializable>> map = CollectionUtil.newMap();

        Set<Class<?>> clzSet = ClassUtil.scanTypeAnnotations(BootInfoHolder.getAppPackage(), broadcastClz);

        for(Class<?> clz: clzSet)
        {
            Broadcast broadcast = clz.getAnnotation(broadcastClz);
            RabbitAttr rabbitBroadcast = clz.getAnnotation(rabbitQueueClz);
            if(rabbitBroadcast == null)
            {
                log.debug("不是 RabbigMQ 的监听器: {}, 略过", broadcast.value());
                continue;
            }

            if(!MessageListener.class.isAssignableFrom(clz))
            {
                continue;
            }

            log.info("注册 RabbitMQ 消息监听器, 监听广播: {}", broadcast.value());

            map.put(clz.getAnnotation(broadcastClz).value(), (MessageListener<Serializable>)SpringContextHolder.getBean(clz));
        }

        return map;
    }
}

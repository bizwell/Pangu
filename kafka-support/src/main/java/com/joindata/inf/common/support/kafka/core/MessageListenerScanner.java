package com.joindata.inf.common.support.kafka.core;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.basic.support.SpringContextHolder;
import com.joindata.inf.common.sterotype.mq.MessageListener;
import com.joindata.inf.common.sterotype.mq.annotation.Topic;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;

public class MessageListenerScanner
{
    private static final Logger log = Logger.get();

    private final Class<Topic> topicClz = Topic.class;

    @SuppressWarnings("unchecked")
    public Map<String, MessageListener<String>> scanStringTopicListener()
    {
        Map<String, MessageListener<String>> map = CollectionUtil.newMap();

        Set<Class<?>> clzSet = ClassUtil.scanTypeAnnotations(BootInfoHolder.getAppPackage(), topicClz);

        for(Class<?> clz: clzSet)
        {
            Topic topic = clz.getAnnotation(topicClz);

            if(!MessageListener.class.isAssignableFrom(clz))
            {
                continue;
            }

            Class<?> innerClz = ClassUtil.getNestedGenericType(clz);
            if(!String.class.equals(innerClz))
            {
                continue;
            }

            log.info("注册 Kafka 文本消息监听器, 监听主题: {}", topic.value());
            map.put(clz.getAnnotation(topicClz).value(), (MessageListener<String>)SpringContextHolder.getBean(clz));
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    public Map<String, MessageListener<Serializable>> scanBinaryTopicListener()
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

            log.info("注册 Kafka 二进制消息监听器, 监听主题: {}", topic.value());
            map.put(clz.getAnnotation(topicClz).value(), (MessageListener<Serializable>)SpringContextHolder.getBean(clz));
        }

        return map;
    }
}

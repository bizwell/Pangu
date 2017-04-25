package com.joindata.inf.common.support.kafka.support.dubbo.protocol;

import java.lang.reflect.Method;
import java.util.Map;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.InvokerListener;
import com.alibaba.dubbo.rpc.RpcException;
import com.joindata.inf.common.sterotype.mq.annotation.Topic;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;

/**
 * 让 Dubbo 支持 Kafka 的消息发送，这是个 Dubbo 的引用拦截器，在声明引用时，将定义的方法缓存起来，供调用拦截器决定消息是否发送到 Kafka
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 20, 2017 12:30:23 PM
 */
public class KafkaMessagingServiceRegistry implements InvokerListener
{
    /** 支持 KafkaTopic 的 Method 会放进来 */
    private static final Map<Method, String> TopicMap = CollectionUtil.newMap();

    public void referred(Invoker<?> invoker) throws RpcException
    {
        Map<Method, Topic> map = ClassUtil.getMethodAnnotationMap(invoker.getInterface(), Topic.class);
        for(Method method: map.keySet())
        {
            TopicMap.put(method, map.get(method).value());
        }
    }

    /**
     * 是否支持 Kafka 消息
     * 
     * @param method 调用的方法
     * @return 如果该方法包含 Topic 注解，返回 true
     */
    static boolean supportTopic(Method method)
    {
        return TopicMap.containsKey(method);
    }

    public void destroyed(Invoker<?> invoker) throws RpcException
    {
    }
}
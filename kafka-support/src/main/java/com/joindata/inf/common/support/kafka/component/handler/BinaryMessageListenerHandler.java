package com.joindata.inf.common.support.kafka.component.handler;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.sterotype.mq.MessageListener;
import com.joindata.inf.common.support.kafka.pojo.BinaryConsumer;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * 二进制消息监听器处理器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 16, 2017 3:06:14 PM
 */
public class BinaryMessageListenerHandler implements MessageListenerHandler
{
    private static Logger log = Logger.get();

    /** 消息接收客户端 */
    private BinaryConsumer<Serializable> consumer;

    /** topic 每次收取多少数据 */
    private Map<String, Integer> messageCountMap;

    /** topic - 消息监听器 */
    private Map<String, MessageListener<Serializable>> listenerMap;

    /** 超时多久 */
    private int consumerTimeout;

    /** 线程池 */
    private ExecutorService executor = null;

    /**
     * 构造二进制消息监听处理器
     */
    public BinaryMessageListenerHandler(Map<String, MessageListener<Serializable>> listenerMap, int consumerTimeout, int receiveCount, int poolSize, String brokerList)
    {
        this.listenerMap = listenerMap;
        this.messageCountMap = CollectionUtil.newMap();

        // 设置每次接收大小
        for(String topic: this.listenerMap.keySet())
        {
            this.messageCountMap.put(topic, receiveCount);
        }

        this.consumerTimeout = consumerTimeout;

        // 创建消息接收器
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", brokerList);
        props.setProperty("group.id", BootInfoHolder.getAppId());
        props.setProperty("key.deserializer", ByteArrayDeserializer.class.getName());
        props.setProperty("value.deserializer", ByteArrayDeserializer.class.getName());

        consumer = new BinaryConsumer<>(props);

        // 初始化线程池
        executor = Executors.newFixedThreadPool(poolSize);

    }

    /**
     * 启动
     */
    public void init()
    {
        final BinaryMessageListenerHandler handler = this;
        executor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                handler.trigger();
            }
        });
    }

    /**
     * 执行收取任务
     */
    public void trigger()
    {
        if(listenerMap.isEmpty())
        {
            return;
        }

        consumer.subscribe(listenerMap.keySet());

        log.info("收取队列 {} 中的二进制消息", consumer.subscription());
        while(true)
        {
            ConsumerRecords<String, Serializable> records = consumer.poll(this.consumerTimeout);
            for(ConsumerRecord<String, Serializable> record: records)
            {
                log.info("取到队列 {} 中的二进制消息, offset: {}", record.topic(), record.offset());

                this.listenerMap.get(record.topic()).onReceive(record.value());
            }
        }
    }
}

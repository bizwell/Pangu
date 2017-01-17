package com.joindata.inf.common.support.kafka.component.handler;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.sterotype.mq.MessageListener;
import com.joindata.inf.common.support.kafka.pojo.StringConsumer;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * 字符串消息监听器处理器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 16, 2017 3:06:14 PM
 */
public class StringMessageListenerHandler implements MessageListenerHandler
{
    private static Logger log = Logger.get();

    /** 消息接收客户端 */
    private StringConsumer consumer;

    /** topic 每次收取多少数据 */
    private Map<String, Integer> messageCountMap;

    /** topic - 消息监听器 */
    private Map<String, MessageListener<String>> listenerMap;

    /** 超时多久 */
    private int consumerTimeout;

    /** 线程池 */
    private ExecutorService executor = null;

    /**
     * 构造二进制消息监听处理器
     */
    public StringMessageListenerHandler(Map<String, MessageListener<String>> listenerMap, int consumerTimeout, int receiveCount, int poolSize, String brokerList)
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
        props.setProperty("key.deserializer", StringDeserializer.class.getName());
        props.setProperty("value.deserializer", StringDeserializer.class.getName());

        consumer = new StringConsumer(props);

        // 初始化线程池
        executor = Executors.newFixedThreadPool(poolSize);
    }

    /**
     * 启动
     */
    public void init()
    {
        final StringMessageListenerHandler handler = this;
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

        log.info("收取队列 {} 中的文本消息", consumer.subscription());
        while(true)
        {
            ConsumerRecords<String, String> records = consumer.poll(this.consumerTimeout);
            for(ConsumerRecord<String, String> record: records)
            {
                log.info("取到队列 {} 中的文本消息, offset: {}", record.topic(), record.offset());

                this.listenerMap.get(record.topic()).onReceive(record.value());
            }
        }
    }
}

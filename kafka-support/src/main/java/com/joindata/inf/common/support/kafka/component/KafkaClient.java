package com.joindata.inf.common.support.kafka.component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.kafka.pojo.BinaryConsumer;
import com.joindata.inf.common.support.kafka.pojo.BinaryProducer;
import com.joindata.inf.common.support.kafka.pojo.StringConsumer;
import com.joindata.inf.common.support.kafka.pojo.StringProducer;
import com.joindata.inf.common.support.kafka.properties.KafkaProperties;
import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * Kafka 客户端
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 11, 2017 10:56:37 AM
 */
@Component
public class KafkaClient
{
    private static final Logger log = Logger.get();

    @Autowired
    private KafkaProperties properties;

    /** 保存与 Topic 对应的 Producer */
    @SuppressWarnings("rawtypes")
    private static final Map<String, Producer> ProducerMap = CollectionUtil.newMap();

    /** 保存与 Topic 对应的 Consumer */
    @SuppressWarnings("rawtypes")
    private static final Map<String, Consumer> ConsumerMap = CollectionUtil.newMap();

    /**
     * 发送文本消息
     * 
     * @param topic 主题
     * @param message 内容（可指定多条）
     */
    public void send(String topic, String... message)
    {
        if(ArrayUtil.isEmpty(message))
        {
            log.warn("发送到 {} 上的主题 {} 的消息为空", properties.getBrokerList(), topic);
            return;
        }

        log.info("向 {} 发送 {} 条文本消息", topic, message.length);

        log.debug("消息内容是：{}", ArrayUtil.join(message));

        StringProducer producer = null;

        // 如果没有该 topic 的 Producer 的缓存，创建一个
        if(ProducerMap.get(topic) == null)
        {
            log.debug("创建目标为 {} 的主题 {} 的文本发送器", properties.getBrokerList(), topic);

            Properties props = new Properties();
            props.setProperty("bootstrap.servers", properties.getBrokerList());
            props.setProperty("key.serializer", StringSerializer.class.getName());
            props.setProperty("value.serializer", StringSerializer.class.getName());

            ProducerMap.put(topic, new StringProducer(props));
        }

        producer = (StringProducer)ProducerMap.get(topic);
        
        for(String msg: message)
        {
            ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, msg);
            producer.send(data);
        }
    }

    /**
     * 发送消息
     * 
     * @param topic 主题
     * @param message 内容，可序列化的任何对象（可指定多条）
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> void send(String topic, T... message)
    {
        if(ArrayUtil.isEmpty(message))
        {
            log.warn("发送到 {} 上的主题 {} 的消息为空", properties.getBrokerList(), topic);
            return;
        }

        log.info("向 {} 发送 {} 条二进制消息", topic, message.length);

        log.debug("消息内容是：{}", message.getClass().getCanonicalName());

        BinaryProducer<T> producer = null;

        // 如果没有该 topic 的 Producer 的缓存，创建一个
        if(ProducerMap.get(topic) == null)
        {
            log.debug("创建目标为 {} 的主题 {} 的二进制发送器", properties.getBrokerList(), topic);

            Properties props = new Properties();
            props.setProperty("bootstrap.servers", properties.getBrokerList());
            props.setProperty("key.serializer", StringSerializer.class.getName());
            props.setProperty("value.serializer", ByteArraySerializer.class.getName());

            ProducerMap.put(topic, new BinaryProducer<T>(props));
        }

        producer = (BinaryProducer<T>)ProducerMap.get(topic);

        // 发消息
        for(T msg: message)
        {
            ProducerRecord<String, T> data = new ProducerRecord<String, T>(topic, msg);
            producer.send(data);
        }
    }

    /**
     * 消费一条文本消息<br />
     * <B>如果没有特殊需求，不要调用这个，十分影响性能</B>
     * 
     * @param topic 主题
     * @return 消息内容
     */
    public String receive(String topic)
    {
        StringConsumer consumer = null;

        // 如果没有该 topic 的 Consumer 的缓存，创建一个
        if(ConsumerMap.get(topic) == null)
        {
            log.debug("创建目标为 {} 的主题 {} 的文本接收器", properties.getBrokerList(), topic);

            Properties props = new Properties();
            props.setProperty("bootstrap.servers", properties.getBrokerList());
            props.setProperty("group.id", BootInfoHolder.getAppId());
            props.setProperty("key.deserializer", StringDeserializer.class.getName());
            props.setProperty("value.deserializer", StringDeserializer.class.getName());

            ConsumerMap.put(topic, new StringConsumer(props));
        }

        consumer = (StringConsumer)ConsumerMap.get(topic);

        consumer.subscribe(CollectionUtil.newHashSet(topic));
        ConsumerRecords<String, String> record = consumer.poll(properties.getConsumeTimeout());

        if(record == null || !record.iterator().hasNext())
        {
            return null;
        }

        return record.iterator().next().value();
    }

    /**
     * 消费多条文本消息<br />
     * <B>如果没有特殊需求，不要调用这个，十分影响性能</B>
     * 
     * @param topic 主题
     * @param count 最多消费多少条
     * @return 文本消息列表
     */
    public List<String> receive(String topic, int count)
    {
        List<String> dataList = CollectionUtil.newList();

        StringConsumer consumer = null;

        // 如果没有该 topic 的 Consumer 的缓存，创建一个
        if(ConsumerMap.get(topic) == null)
        {
            log.debug("创建目标为 {} 的主题 {} 的文本接收器", properties.getBrokerList(), topic);

            Properties props = new Properties();
            props.setProperty("bootstrap.servers", properties.getBrokerList());
            props.setProperty("group.id", BootInfoHolder.getAppId());
            props.setProperty("key.deserializer", StringDeserializer.class.getName());
            props.setProperty("value.deserializer", StringDeserializer.class.getName());

            ConsumerMap.put(topic, new StringConsumer(props));
        }

        consumer = (StringConsumer)ConsumerMap.get(topic);

        consumer.subscribe(CollectionUtil.newHashSet(topic));
        ConsumerRecords<String, String> records = consumer.poll(properties.getConsumeTimeout());

        if(records == null)
        {
            return dataList;
        }

        int i = 0;
        for(ConsumerRecord<String, String> record: records)
        {
            if(++i == count)
            {
                continue;
            }

            dataList.add(record.value());
        }

        return dataList;

    }

    /**
     * 消费一条二进制消息<br />
     * <B>如果没有特殊需求，不要调用这个，十分影响性能</B>
     * 
     * @param topic 主题
     * @param clz 接收到消息后转化成什么类型的对象
     * @return 消息内容
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T receive(String topic, Class<T> clz)
    {
        BinaryConsumer<T> consumer = null;

        // 如果没有该 topic 的 Consumer 的缓存，创建一个
        if(ConsumerMap.get(topic) == null)
        {
            log.debug("创建目标为 {} 的主题 {} 的文本接收器", properties.getBrokerList(), topic);

            Properties props = new Properties();
            props.setProperty("bootstrap.servers", properties.getBrokerList());
            props.setProperty("group.id", BootInfoHolder.getAppId());
            props.setProperty("key.deserializer", ByteArrayDeserializer.class.getName());
            props.setProperty("value.deserializer", ByteArrayDeserializer.class.getName());

            ConsumerMap.put(topic, new BinaryConsumer<T>(props));
        }

        consumer = (BinaryConsumer<T>)ConsumerMap.get(topic);

        consumer.subscribe(CollectionUtil.newHashSet(topic));
        ConsumerRecords<String, T> record = consumer.poll(properties.getConsumeTimeout());

        if(record == null || !record.iterator().hasNext())
        {
            return null;
        }

        return record.iterator().next().value();
    }

    /**
     * 消费多条二进制消息<br />
     * <B>如果没有特殊需求，不要调用这个，十分影响性能</B>
     * 
     * @param topic 主题
     * @param clz 接收到消息后转化成什么类型的对象
     * @return 消息内容列表
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> List<T> receive(String topic, int count, Class<T> clz)
    {
        List<T> dataList = CollectionUtil.newList();

        BinaryConsumer<T> consumer = null;

        // 如果没有该 topic 的 Consumer 的缓存，创建一个
        if(ConsumerMap.get(topic) == null)
        {
            log.debug("创建目标为 {} 的主题 {} 的文本接收器", properties.getBrokerList(), topic);

            Properties props = new Properties();
            props.setProperty("bootstrap.servers", properties.getBrokerList());
            props.setProperty("group.id", BootInfoHolder.getAppId());
            props.setProperty("key.deserializer", ByteArrayDeserializer.class.getName());
            props.setProperty("value.deserializer", ByteArrayDeserializer.class.getName());

            ConsumerMap.put(topic, new BinaryConsumer<T>(props));
        }

        consumer = (BinaryConsumer<T>)ConsumerMap.get(topic);

        consumer.subscribe(CollectionUtil.newHashSet(topic));
        ConsumerRecords<String, T> records = consumer.poll(properties.getConsumeTimeout());

        if(records == null)
        {
            return dataList;
        }

        int i = 0;
        for(ConsumerRecord<String, T> record: records)
        {
            if(++i == count)
            {
                continue;
            }

            dataList.add(record.value());
        }

        return dataList;
    }

}

package com.joindata.inf.common.support.kafka.pojo;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;

/**
 * 字符串发送器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 17, 2017 3:10:05 PM
 */
public class StringProducer extends KafkaProducer<String, String>
{

    public StringProducer(Properties properties)
    {
        super(properties);
    }

}

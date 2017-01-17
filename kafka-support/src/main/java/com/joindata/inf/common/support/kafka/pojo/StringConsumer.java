package com.joindata.inf.common.support.kafka.pojo;

import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class StringConsumer extends KafkaConsumer<String, String>
{

    public StringConsumer(Properties properties)
    {
        super(properties);
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
    }

}

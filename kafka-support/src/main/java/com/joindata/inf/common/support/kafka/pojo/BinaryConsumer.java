package com.joindata.inf.common.support.kafka.pojo;

import java.io.Serializable;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public class BinaryConsumer<T extends Serializable> extends KafkaConsumer<String, T>
{
    public BinaryConsumer(Properties properties)
    {
        super(properties);
    }

}

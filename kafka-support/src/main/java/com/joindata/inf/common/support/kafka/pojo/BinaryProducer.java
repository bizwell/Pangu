package com.joindata.inf.common.support.kafka.pojo;

import java.io.Serializable;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;

public class BinaryProducer<T extends Serializable> extends KafkaProducer<String, T>
{
    public BinaryProducer(Properties properties)
    {
        super(properties);
    }
}

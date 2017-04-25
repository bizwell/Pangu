package com.joindata.inf.common.support.kafka.support.dubbo.protocol;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.protocol.AbstractExporter;

/**
 * Kafka 的服务导出
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 20, 2017 3:54:35 PM
 * @param <T>
 */
public class KafkaExporter<T> extends AbstractExporter<T>
{

    public KafkaExporter(Invoker<T> invoker)
    {
        super(invoker);
    }

}
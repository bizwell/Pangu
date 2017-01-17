package com.joindata.inf.common.support.kafka.properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

@Service
@Scope("singleton")
@DisconfFile(filename = "kafka.properties")
public class KafkaProperties
{
    /** Kafka 实例连接串 */
    private String brokerList;

    /** 消费读取超时时间 */
    private Integer consumeTimeout;

    @DisconfFileItem(name = "broker.list", associateField = "brokerList")
    public String getBrokerList()
    {
        return brokerList;
    }

    public void setBrokerList(String brokerList)
    {
        this.brokerList = brokerList;
    }

    @DisconfFileItem(name = "consume.timeout", associateField = "consumeTimeout")
    public Integer getConsumeTimeout()
    {
        return consumeTimeout;
    }

    public void setConsumeTimeout(Integer consumeTimeout)
    {
        this.consumeTimeout = consumeTimeout;
    }

}
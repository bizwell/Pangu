package com.joindata.inf.common.support.email.properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * 邮件配置文件
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017-03-29
 */
@Service
@Scope("singleton")
@DisconfFile(filename = "email.properties")
public class EmailProperties
{
    /** ons=阿里云消息服务，activemq=本公司使用的消息服务 */
    private String serverKind;

    /** ActiveMQ 消息队列名称 */
    private String amqQueueName;

    /** ActiveMQ 服务器连接串 */
    private String mqBroker;

    /** ONS 配置 */
    private String onsProducerId;

    /** ONS 配置 */
    private String onsAccessKey;

    /** ONS 配置 */
    private String onsSecretKey;

    private String onsQueueName;

    /** 应用系统id */
    private String appId;

    @DisconfFileItem(name = "server.kind", associateField = "serverKind")
    public String getServerKind()
    {
        return serverKind;
    }

    public void setServerKind(String serverKind)
    {
        this.serverKind = serverKind;
    }

    @DisconfFileItem(name = "mq.broker", associateField = "mqBroker")
    public String getMqBroker()
    {
        return mqBroker;
    }

    @DisconfFileItem(name = "amq.queue.name", associateField = "amqQueueName")
    public String getAmqQueueName()
    {
        return amqQueueName;
    }

    public void setAmqQueueName(String amqQueueName)
    {
        this.amqQueueName = amqQueueName;
    }

    @DisconfFileItem(name = "ons.queue.name", associateField = "onsQueueName")
    public String getOnsQueueName()
    {
        return onsQueueName;
    }

    @DisconfFileItem(name = "app.id", associateField = "appId")
    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public void setOnsQueueName(String onsQueueName)
    {
        this.onsQueueName = onsQueueName;
    }

    @DisconfFileItem(name = "ons.producerId", associateField = "onsProducerId")
    public String getOnsProducerId()
    {
        return onsProducerId;
    }

    public void setOnsProducerId(String onsProducerId)
    {
        this.onsProducerId = onsProducerId;
    }

    @DisconfFileItem(name = "ons.accessKey", associateField = "onsAccessKey")
    public String getOnsAccessKey()
    {
        return onsAccessKey;
    }

    public void setOnsAccessKey(String onsAccessKey)
    {
        this.onsAccessKey = onsAccessKey;
    }

    @DisconfFileItem(name = "ons.secretKey", associateField = "onsSecretKey")
    public String getOnsSecretKey()
    {
        return onsSecretKey;
    }

    public void setOnsSecretKey(String onsSecretKey)
    {
        this.onsSecretKey = onsSecretKey;
    }

}

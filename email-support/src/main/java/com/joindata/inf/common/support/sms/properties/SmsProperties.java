package com.joindata.inf.common.support.sms.properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * SMS 配置文件
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 22, 2017 10:35:07 AM
 */
@Service
@Scope("singleton")
@DisconfFile(filename = "sms.properties")
public class SmsProperties
{
    /** ons=阿里云消息服务，activemq=本公司使用的消息服务 */
    private String serverKind;

    /** ActiveMQ 服务器连接串 */
    private String mqBroker;

    /** ActiveMQ 消息队列配置 */
    private String newQueue;

    /** ActiveMQ 消息队列配置 */
    private String batchQueue;

    /** ActiveMQ 消息队列配置 */
    private String validateQueue;

    /** ActiveMQ 消息队列配置 */
    private String ivrQueue;

    /** ActiveMQ 消息队列配置 */
    private String mailQueue;

    /** ONS 配置 */
    private String onsProducerId;

    /** ONS 配置 */
    private String onsAccessKey;

    /** ONS 配置 */
    private String onsSecretKey;

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


    public void setMqBroker(String mqBroker)
    {
        this.mqBroker = mqBroker;
    }

    @DisconfFileItem(name = "mq.queue.new", associateField = "newQueue")
    public String getNewQueue()
    {
        return newQueue;
    }

    public void setNewQueue(String newQueue)
    {
        this.newQueue = newQueue;
    }

    @DisconfFileItem(name = "mq.queue.batch", associateField = "batchQueue")
    public String getBatchQueue()
    {
        return batchQueue;
    }

    public void setBatchQueue(String batchQueue)
    {
        this.batchQueue = batchQueue;
    }

    @DisconfFileItem(name = "mq.queue.validate", associateField = "validateQueue")
    public String getValidateQueue()
    {
        return validateQueue;
    }

    public void setValidateQueue(String validateQueue)
    {
        this.validateQueue = validateQueue;
    }

    @DisconfFileItem(name = "mq.queue.ivr", associateField = "ivrQueue")
    public String getIvrQueue()
    {
        return ivrQueue;
    }

    public void setIvrQueue(String ivrQueue)
    {
        this.ivrQueue = ivrQueue;
    }

    @DisconfFileItem(name = "mq.queue.mail", associateField = "mailQueue")
    public String getMailQueue()
    {
        return mailQueue;
    }

    public void setMailQueue(String mailQueue)
    {
        this.mailQueue = mailQueue;
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

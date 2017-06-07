package com.joindata.inf.common.support.paho.properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

@Service
@Scope("singleton")
@DisconfFile(filename = "paho.properties")
public class PahoProperties
{
    /** 服务器地址列表 */
    private String brokerList;

    /** 消费超时时间 */
    private Integer consumeTimeout;

    /** 用户名 */
    private String brokerUsername;

    /** 密码 */
    private String brokerPassword;

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

    @DisconfFileItem(name = "broker.username", associateField = "brokerUsername")
    public String getBrokerUsername()
    {
        return brokerUsername;
    }

    public void setBrokerUsername(String brokerUsername)
    {
        this.brokerUsername = brokerUsername;
    }

    @DisconfFileItem(name = "broker.password", associateField = "brokerPassword")
    public String getBrokerPassword()
    {
        return brokerPassword;
    }

    public void setBrokerPassword(String brokerPassword)
    {
        this.brokerPassword = brokerPassword;
    }
}
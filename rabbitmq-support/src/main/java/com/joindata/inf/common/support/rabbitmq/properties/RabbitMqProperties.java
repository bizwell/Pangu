package com.joindata.inf.common.support.rabbitmq.properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * RabbitMQ 配置属性
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 12, 2017 11:16:04 AM
 */
@Service
@Scope("singleton")
@DisconfFile(filename = "rabbitmq.properties")
public class RabbitMqProperties
{
    private String host;

    private int port;

    private String username;

    private String password;

    @DisconfFileItem(name = "rabbitmq.host", associateField = "host")
    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    @DisconfFileItem(name = "rabbitmq.port", associateField = "port")
    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    @DisconfFileItem(name = "rabbitmq.username", associateField = "username")
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @DisconfFileItem(name = "rabbitmq.password", associateField = "password")
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
